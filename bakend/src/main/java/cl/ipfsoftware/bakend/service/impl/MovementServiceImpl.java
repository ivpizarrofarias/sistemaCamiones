package cl.ipfsoftware.bakend.service.impl;


import cl.ipfsoftware.bakend.exception.BusinessValidationException;
import cl.ipfsoftware.bakend.exception.EntityNotFoundException;
import cl.ipfsoftware.bakend.exception.InternalServerException;
import cl.ipfsoftware.bakend.model.dto.MovementDTO;
import cl.ipfsoftware.bakend.model.entities.Movimiento;
import cl.ipfsoftware.bakend.model.mapper.MovementMapper;
import cl.ipfsoftware.bakend.persistence.repositories.MovimientoRepository;
import cl.ipfsoftware.bakend.service.MovementService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovementServiceImpl implements MovementService {

    private final MovimientoRepository movimientoRepository;
    private final MovementMapper movementMapper;
    private final Validator validator;

    public MovementServiceImpl(MovimientoRepository movimientoRepository, MovementMapper movementMapper, Validator validator) {
        this.movimientoRepository = movimientoRepository;
        this.movementMapper = movementMapper;
        this.validator = validator;
    }

    @Override
    public MovementDTO createMovement(MovementDTO movementDTO) {
        Movimiento movimiento;
        try {
            Set<ConstraintViolation<MovementDTO>> violations = validator.validate(movementDTO);
            if (!violations.isEmpty()) {
                String message = violations.stream()
                        .map(violation -> String.format("%s: %s", violation.getPropertyPath(), violation.getMessage()))
                        .collect(Collectors.joining(", "));
                throw new BusinessValidationException("Validación de movimiento fallida: " + message);
            }
            movimiento = movementMapper.toMovement(movementDTO);
            movimiento = movimientoRepository.save(movimiento);
        } catch (DuplicateKeyException e) {
            throw new BusinessValidationException("Error al guardar el movimiento: " + e.getMessage());
        } catch (ConstraintViolationException e) {
            throw new BusinessValidationException("Error de validación del movimiento: " + e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException("Error inesperado al guardar el movimiento: " + e.getMessage());
        }
        MovementDTO resultDTO = movementMapper.toMovementDTO(movimiento);
        System.out.println("Movimiento DTO creado: " + resultDTO);
        return resultDTO;
    }

    @Override
    public List<MovementDTO> getAllMovements() {
        return movimientoRepository.findAll().stream()
                .map(movementMapper::toMovementDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MovementDTO> updateMovement(Integer id, MovementDTO movementDTO) throws Exception {
        try {
            Set<ConstraintViolation<MovementDTO>> violations = validator.validate(movementDTO);
            if (!violations.isEmpty()) {
                String message = violations.stream()
                        .map(violation -> String.format("%s: %s", violation.getPropertyPath(), violation.getMessage()))
                        .collect(Collectors.joining(", "));
                throw new BusinessValidationException("Validación de movimiento fallida: " + message);
            }
            Movimiento movimientoToUpdate = movimientoRepository.findByIdMovimiento(id)
                    .orElseThrow(() -> new EntityNotFoundException("Movimiento no encontrado con ID: " + id));
            movimientoToUpdate.setFechaEmision(movementDTO.getEmissionDate());
            movimientoToUpdate.setNumeroDocumento(movementDTO.getDocumentNumber());
            movimientoToUpdate.setTipoDocumento(movementDTO.getDocumentType());
            movimientoToUpdate.setEstadoFisico(movementDTO.getPhysicalState());
            movimientoToUpdate.setTipoTamanio(movementDTO.getSizeType());
            movimientoToUpdate.setFechaHoraEntrada(movementDTO.getEntryDateTime());
            movimientoToUpdate.setFechaHoraSalida(movementDTO.getExitDateTime());
            movimientoToUpdate.getCamion().setIdCamion(movementDTO.getTruckId());
            movimientoToUpdate.getChofer().setIdChofer(movementDTO.getDriverId());
            movimientoToUpdate.getCliente().setIdCliente(movementDTO.getClientId());
            movimientoToUpdate.getNave().setIdNave(movementDTO.getShipId());
            movimientoToUpdate.getPuerto().setIdPuerto(movementDTO.getPortId());
            movimientoToUpdate.getTransporteTerrestre().setIdTransporte(movementDTO.getTerrestrialTransportId());
            movimientoToUpdate.getContenedor().setIdContenedor(movementDTO.getContainerId());
            movimientoRepository.save(movimientoToUpdate);
            MovementDTO resultDTO = movementMapper.toMovementDTO(movimientoToUpdate);
            System.out.println("El movimiento ha sido modificado con éxito");
            return Optional.of(resultDTO);
        } catch (DuplicateKeyException e) {
            throw new BusinessValidationException("Error al actualizar el movimiento: " + e.getMessage());
        } catch (ConstraintViolationException e) {
            throw new BusinessValidationException("Error de validación del movimiento: " + e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Error al actualizar el movimiento: " + e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException("Error inesperado al actualizar el movimiento: " + e.getMessage());
        }
    }

    @Override
    public Optional<MovementDTO> getMovementById(Integer id) {
        return Optional.ofNullable(id)
                .map(movimientoRepository::findByIdMovimiento)
                .orElseThrow(() -> new IllegalArgumentException("Movimiento no encontrado"))
                .map(movementMapper::toMovementDTO);
    }

    @Override
    public void deleteMovementById(Integer id) {
        Optional<Movimiento> movimientoOptional = movimientoRepository.findById(id);
        if (movimientoOptional.isPresent()) {
            movimientoRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Movimiento no encontrado con ID: " + id);
        }
    }

    @Override
    public Boolean existsMovementById(Integer id) {
        return movimientoRepository.existsByIdMovimiento(id);
    }

    @Override
    public List<MovementDTO> getMovementsByTruckId(Integer truckId) {
        return movimientoRepository.findByCamionIdCamion(truckId).stream()
                .map(movementMapper::toMovementDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MovementDTO> getMovementsByDriverId(Integer driverId) {
        return movimientoRepository.findByChoferIdChofer(driverId).stream()
                .map(movementMapper::toMovementDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MovementDTO> getMovementsByClientId(Integer clientId) {
        return movimientoRepository.findByClienteIdCliente(clientId).stream()
                .map(movementMapper::toMovementDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MovementDTO> getMovementsByShipId(Integer shipId) {
        return movimientoRepository.findByNaveIdNave(shipId).stream()
                .map(movementMapper::toMovementDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MovementDTO> getMovementsByPortId(Integer portId) {
        return movimientoRepository.findByPuertoIdPuerto(portId).stream()
                .map(movementMapper::toMovementDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MovementDTO> getMovementsByGroundTransportId(Integer groundTransportId) {
        return movimientoRepository.findByTransporteTerrestreIdTransporte(groundTransportId).stream()
                .map(movementMapper::toMovementDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MovementDTO> getMovementsByContainerId(Integer containerId) {
        return movimientoRepository.findByContenedorIdContenedor(containerId).stream()
                .map(movementMapper::toMovementDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void exportExcelMovements(HttpServletResponse response) throws Exception {
        List<Movimiento> movements = movimientoRepository.findAll();
        if (movements.isEmpty()) {
            throw new EntityNotFoundException("No se encontraron movimientos.");
        }

        // Crear el libro de Excel
        Workbook workbook = new XSSFWorkbook();
        var sheet = workbook.createSheet("Movements");

        // Ajustar márgenes de la hoja
        sheet.setMargin(Sheet.LeftMargin, 0.5); // Margen izquierdo
        sheet.setMargin(Sheet.RightMargin, 0.5); // Margen derecho
        sheet.setMargin(Sheet.TopMargin, 0.75); // Margen superior
        sheet.setMargin(Sheet.BottomMargin, 0.75); // Margen inferior

        // Fuentes para el título y subtítulo
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 16);

        Font subtitleFont = workbook.createFont();
        subtitleFont.setBold(true);
        subtitleFont.setFontHeightInPoints((short) 14);

        // Estilo para el título "Transportes Muñoz"
        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // Estilo para el subtítulo "Listado de Movimientos"
        CellStyle subtitleStyle = workbook.createCellStyle();
        subtitleStyle.setFont(subtitleFont);
        subtitleStyle.setAlignment(HorizontalAlignment.CENTER);
        subtitleStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        subtitleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Crear fila para el título
        var titleRow = sheet.createRow(0);
        titleRow.setHeightInPoints(30); // Altura de la fila del título
        var titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Transportes Muñoz");
        titleCell.setCellStyle(titleStyle);

        // Combinar celdas para el título
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 13)); // Combinar 9 celdas para el título

        // Crear subtítulo "Listado de Movimientos"
        var subtitleRow = sheet.createRow(1);
        var subtitleCell = subtitleRow.createCell(0);
        subtitleCell.setCellValue("Listado de Movimientos");
        subtitleCell.setCellStyle(subtitleStyle);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 13)); // Combinar celdas de la fila del subtítulo

        // Crear estilo para encabezados
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setBorderTop(BorderStyle.THIN); // Borde superior
        headerStyle.setBorderBottom(BorderStyle.THIN); // Borde inferior
        headerStyle.setBorderLeft(BorderStyle.THIN); // Borde izquierdo
        headerStyle.setBorderRight(BorderStyle.THIN); // Borde derecho

        // Crear fila de encabezados
        var headerRow = sheet.createRow(2);
        String[] headers = {
                "Fecha Emisión","N° Documento","Tipo Documento", "Contenedor","Estado Físico", "Tamaño", "Nombre Nave","Cliente",
                "Nombre Chofer","Patente Camión","Transportista", "Destino", "Fecha Entrada", "Fecha Salida"
        };
        for (int i = 0; i < headers.length; i++) {
            var cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Estilo para la fecha
        CellStyle dateStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy HH:mm"));
        dateStyle.setAlignment(HorizontalAlignment.CENTER); // Centrar el contenido
        dateStyle.setBorderTop(BorderStyle.THIN); // Borde superior
        dateStyle.setBorderBottom(BorderStyle.THIN); // Borde inferior
        dateStyle.setBorderLeft(BorderStyle.THIN); // Borde izquierdo
        dateStyle.setBorderRight(BorderStyle.THIN); // Borde derecho

        // Estilo para las celdas de datos
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setAlignment(HorizontalAlignment.CENTER); // Centrar el contenido
        dataStyle.setBorderTop(BorderStyle.THIN); // Borde superior
        dataStyle.setBorderBottom(BorderStyle.THIN); // Borde inferior
        dataStyle.setBorderLeft(BorderStyle.THIN); // Borde izquierdo
        dataStyle.setBorderRight(BorderStyle.THIN); // Borde derecho

        // Agregar contenido
        for (int i = 0; i < movements.size(); i++) {
            Movimiento movimiento = movements.get(i);
            var row = sheet.createRow(i + 3); // Comienza desde la fila 3

            // Fecha Emisión
            var emissionDateCell = row.createCell(0);
            if (movimiento.getFechaEmision() != null) {
                emissionDateCell.setCellValue(movimiento.getFechaEmision());
                emissionDateCell.setCellStyle(dateStyle);
            } else {
                emissionDateCell.setCellValue("");
            }

            //"N° Documento"
            var ndocumentTypeCell = row.createCell(1);
            ndocumentTypeCell.setCellValue(movimiento.getNumeroDocumento()!=null ? movimiento.getNumeroDocumento() : "");
            ndocumentTypeCell.setCellStyle(dataStyle);
            // Tipo documento
            var documentTypeCell = row.createCell(2);
            documentTypeCell.setCellValue(movimiento.getTipoDocumento() != null ? movimiento.getTipoDocumento().toString() : "");
            documentTypeCell.setCellStyle(dataStyle);

            // Código del Contenedor
            var containerCodeCell = row.createCell(3);
            containerCodeCell.setCellValue(movimiento.getContenedor() != null ? movimiento.getContenedor().getCodigoContenedor() : "");
            containerCodeCell.setCellStyle(dataStyle);
            //"Estado Físico",
            var stateCell = row.createCell(4);
            stateCell.setCellValue(movimiento.getEstadoFisico() !=null ? movimiento.getEstadoFisico().name() :"" );
            stateCell.setCellStyle(dataStyle);

            // "Tamaño"
            var typeCell = row.createCell(5);
            typeCell.setCellValue(movimiento.getTipoTamanio() !=null ? movimiento.getTipoTamanio().name() :"");
            typeCell.setCellStyle(dataStyle);

            // Nombre de la Nave
            var shipNameCell = row.createCell(6);
            shipNameCell.setCellValue(movimiento.getNave() != null ? movimiento.getNave().getNombreBarco() : "");
            shipNameCell.setCellStyle(dataStyle);

            // cliente
            var clientNameCell = row.createCell(7);
            clientNameCell.setCellValue(movimiento.getCliente().getNombre() !=null ? movimiento.getCliente().getNombre() :"");
            clientNameCell.setCellStyle(dataStyle);

            // Nombre del Chofer
            var driverNameCell = row.createCell(8);
            driverNameCell.setCellValue(movimiento.getChofer() != null ? movimiento.getChofer().getNombre() : "");
            driverNameCell.setCellStyle(dataStyle);

            // Patente del Camión
            var truckLicensePlateCell = row.createCell(9);
            truckLicensePlateCell.setCellValue(movimiento.getCamion() != null ? movimiento.getCamion().getPatente() : "");
            truckLicensePlateCell.setCellStyle(dataStyle);

            //transporte
            var transportCell = row.createCell(10);
            transportCell.setCellValue(movimiento.getTransporteTerrestre().getNombreTransportista() !=null ?
                    movimiento.getTransporteTerrestre().getNombreTransportista() :"");
            transportCell.setCellStyle(dataStyle);


            // Destino del Viaje
            var destinationCell = row.createCell(11);
            destinationCell.setCellValue(movimiento.getPuerto() != null ? movimiento.getPuerto().getNombre() : "");
            destinationCell.setCellStyle(dataStyle);

            // Fecha Entrada
            var entryDateTimeCell = row.createCell(12);
            if (movimiento.getFechaHoraEntrada() != null) {
                entryDateTimeCell.setCellValue(movimiento.getFechaHoraEntrada());
                entryDateTimeCell.setCellStyle(dateStyle);
            } else {
                entryDateTimeCell.setCellValue("");
            }

            // Fecha Salida
            var exitDateTimeCell = row.createCell(13);
            if (movimiento.getFechaHoraSalida() != null) {
                exitDateTimeCell.setCellValue(movimiento.getFechaHoraSalida());
                exitDateTimeCell.setCellStyle(dateStyle);
            } else {
                exitDateTimeCell.setCellValue("");
            }


        }

        // Ajustar ancho de las columnas
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1000); // Aumentar el ancho de las columnas
        }

        // Configurar respuesta HTTP
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=movements.xlsx");
        try (OutputStream os = response.getOutputStream()) {
            workbook.write(os);
        } finally {
            workbook.close();
        }
    }

    @Override
    public void exportExcelMovementsByDriver(HttpServletResponse response, Integer driverId) throws IOException {
        // Obtener los movimientos por chofer
        List<Movimiento> movements = movimientoRepository.findByChoferIdChofer(driverId);
        if (movements.isEmpty()) {
            throw new EntityNotFoundException("No se encontraron movimientos para el chofer con ID: " + driverId);
        }

        // Crear el libro de Excel
        Workbook workbook = new XSSFWorkbook();
        var sheet = workbook.createSheet("MovimientosPorChofer");

        // Ajustar márgenes de la hoja
        sheet.setMargin(Sheet.LeftMargin, 0.5); // Margen izquierdo
        sheet.setMargin(Sheet.RightMargin, 0.5); // Margen derecho
        sheet.setMargin(Sheet.TopMargin, 0.75); // Margen superior
        sheet.setMargin(Sheet.BottomMargin, 0.75); // Margen inferior

        // Fuentes para el título y subtítulo
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 16);

        Font subtitleFont = workbook.createFont();
        subtitleFont.setBold(true);
        subtitleFont.setFontHeightInPoints((short) 14);

        // Estilo para el título "Transportes Muñoz"
        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // Estilo para el subtítulo "Listado de Movimientos por Chofer"
        CellStyle subtitleStyle = workbook.createCellStyle();
        subtitleStyle.setFont(subtitleFont);
        subtitleStyle.setAlignment(HorizontalAlignment.CENTER);
        subtitleStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        subtitleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Crear fila para el título
        var titleRow = sheet.createRow(0);
        titleRow.setHeightInPoints(30); // Altura de la fila del título
        var titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Transportes Muñoz");
        titleCell.setCellStyle(titleStyle);

        // Combinar celdas para el título
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 13)); // Combinar 9 celdas para el título

        // Crear subtítulo "Listado de Movimientos por Chofer"
        var subtitleRow = sheet.createRow(1);
        var subtitleCell = subtitleRow.createCell(0);
        subtitleCell.setCellValue("Listado de Movimientos por Chofer");
        subtitleCell.setCellStyle(subtitleStyle);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 13)); // Combinar celdas de la fila del subtítulo

        // Crear estilo para encabezados
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setBorderTop(BorderStyle.THIN); // Borde superior
        headerStyle.setBorderBottom(BorderStyle.THIN); // Borde inferior
        headerStyle.setBorderLeft(BorderStyle.THIN); // Borde izquierdo
        headerStyle.setBorderRight(BorderStyle.THIN); // Borde derecho

        // Crear fila de encabezados
        var headerRow = sheet.createRow(2);
        String[] headers = {
                "Fecha Emisión", "Nombre Chofer","N° Documento","Tipo Documento", "Contenedor","Estado Físico", "Tamaño", "Nombre Nave","Cliente",
                "Patente Camión","Transporte", "Destino", "Fecha Entrada", "Fecha Salida"
        };
        for (int i = 0; i < headers.length; i++) {
            var cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Estilo para la fecha
        CellStyle dateStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy HH:mm"));
        dateStyle.setAlignment(HorizontalAlignment.CENTER); // Centrar el contenido
        dateStyle.setBorderTop(BorderStyle.THIN); // Borde superior
        dateStyle.setBorderBottom(BorderStyle.THIN); // Borde inferior
        dateStyle.setBorderLeft(BorderStyle.THIN); // Borde izquierdo
        dateStyle.setBorderRight(BorderStyle.THIN); // Borde derecho

        // Estilo para las celdas de datos
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setAlignment(HorizontalAlignment.CENTER); // Centrar el contenido
        dataStyle.setBorderTop(BorderStyle.THIN); // Borde superior
        dataStyle.setBorderBottom(BorderStyle.THIN); // Borde inferior
        dataStyle.setBorderLeft(BorderStyle.THIN); // Borde izquierdo
        dataStyle.setBorderRight(BorderStyle.THIN); // Borde derecho

        // Agregar contenido
        for (int i = 0; i < movements.size(); i++) {
            Movimiento movimiento = movements.get(i);
            var row = sheet.createRow(i + 3); // Comienza desde la fila 3

            // Fecha Emisión
            var emissionDateCell = row.createCell(0);
            if (movimiento.getFechaEmision() != null) {
                emissionDateCell.setCellValue(movimiento.getFechaEmision());
                emissionDateCell.setCellStyle(dateStyle);
            } else {
                emissionDateCell.setCellValue("");
            }


            // Nombre del Chofer
            var driverNameCell = row.createCell(1);
            driverNameCell.setCellValue(movimiento.getChofer() != null ? movimiento.getChofer().getNombre(): "");
            driverNameCell.setCellStyle(dataStyle);

            //"N° Docuemento
            var ndocumentTypeCell = row.createCell(2);
            ndocumentTypeCell.setCellValue(movimiento.getNumeroDocumento()!=null ? movimiento.getNumeroDocumento() : "");
            ndocumentTypeCell.setCellStyle(dataStyle);

            // Tipo Documento
            var documentTypeCell = row.createCell(3);
            documentTypeCell.setCellValue(movimiento.getTipoDocumento() != null ? movimiento.getTipoDocumento().toString() : "");
            documentTypeCell.setCellStyle(dataStyle);

            // Código del Contenedor
            var containerCodeCell = row.createCell(4);
            containerCodeCell.setCellValue(movimiento.getContenedor() != null ? movimiento.getContenedor().getCodigoContenedor() : "");
            containerCodeCell.setCellStyle(dataStyle);

            //"Estado Físico",
            var stateCell = row.createCell(5);
            stateCell.setCellValue(movimiento.getEstadoFisico() !=null ? movimiento.getEstadoFisico().name() :"" );
            stateCell.setCellStyle(dataStyle);

            // "Tamaño"
            var typeCell = row.createCell(6);
            typeCell.setCellValue(movimiento.getTipoTamanio() !=null ? movimiento.getTipoTamanio().name() :"");
            typeCell.setCellStyle(dataStyle);
            // Nombre de la Nave
            var shipNameCell = row.createCell(7);
            shipNameCell.setCellValue(movimiento.getNave() != null ? movimiento.getNave().getNombreBarco() : "");
            shipNameCell.setCellStyle(dataStyle);

            //Cliente

            var clientNameCell = row.createCell(8);
            clientNameCell.setCellValue(movimiento.getCliente().getNombre() !=null ? movimiento.getCliente().getNombre() :"");
            clientNameCell.setCellStyle(dataStyle);

            // Patente del Camión
            var truckLicensePlateCell = row.createCell(9);
            truckLicensePlateCell.setCellValue(movimiento.getCamion() != null ? movimiento.getCamion().getPatente() : "");
            truckLicensePlateCell.setCellStyle(dataStyle);

            //transporte
            var transportCell = row.createCell(10);
            transportCell.setCellValue(movimiento.getTransporteTerrestre().getNombreTransportista() !=null ?
                    movimiento.getTransporteTerrestre().getNombreTransportista() :"");
             transportCell.setCellStyle(dataStyle);


            // Destino del Viaje
            var destinationCell = row.createCell(11);
            destinationCell.setCellValue(movimiento.getPuerto() != null ? movimiento.getPuerto().getNombre() : "");
            destinationCell.setCellStyle(dataStyle);

            // Fecha Entrada
            var entryDateTimeCell = row.createCell(12);
            if (movimiento.getFechaHoraEntrada() != null) {
                entryDateTimeCell.setCellValue(movimiento.getFechaHoraEntrada());
                entryDateTimeCell.setCellStyle(dateStyle);
            } else {
                entryDateTimeCell.setCellValue("");
            }

            // Fecha Salida
            var exitDateTimeCell = row.createCell(13);
            if (movimiento.getFechaHoraSalida() != null) {
                exitDateTimeCell.setCellValue(movimiento.getFechaHoraSalida());
                exitDateTimeCell.setCellStyle(dateStyle);
            } else {
                exitDateTimeCell.setCellValue("");
            }


        }

        // Ajustar ancho de las columnas
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1000); // Aumentar el ancho de las columnas
        }

        // Configurar respuesta HTTP
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=movements_by_driver_" + driverId + ".xlsx");
        try (OutputStream os = response.getOutputStream()) {
            workbook.write(os);
        } finally {
            workbook.close();
        }
    }

    @Override
    public List<MovementDTO> searchMovements(String searchTerm) {
        List<Movimiento> movements = movimientoRepository.findByNumeroDocumentoContainingIgnoreCaseOrClienteNombreContainingIgnoreCase(
                searchTerm, searchTerm
        );
        return movements.stream()
                .map(movementMapper::toMovementDTO)
                .collect(Collectors.toList());
    }
}