
package cl.ipfsoftware.bakend.service.impl;


import cl.ipfsoftware.bakend.exception.BusinessValidationException;
import cl.ipfsoftware.bakend.exception.EntityNotFoundException;
import cl.ipfsoftware.bakend.exception.InternalServerException;
import cl.ipfsoftware.bakend.model.dto.ValuationDTO;
import cl.ipfsoftware.bakend.model.entities.Valorizacion;
import cl.ipfsoftware.bakend.model.mapper.ValuationMapper;
import cl.ipfsoftware.bakend.persistence.repositories.ValorizacionRepository;
import cl.ipfsoftware.bakend.service.ValuationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ValuationServiceImpl implements ValuationService {

    private final ValorizacionRepository valuationRepository;
    private final ValuationMapper valuationMapper;
    private final Validator validator;

    @Autowired
    public ValuationServiceImpl(ValorizacionRepository valuationRepository, ValuationMapper valuationMapper, Validator validator) {
        this.valuationRepository = valuationRepository;
        this.valuationMapper = valuationMapper;
        this.validator = validator;
    }

    @Override
    public ValuationDTO createValuation(ValuationDTO valuationDTO) {
        Valorizacion valorizacion;
        try {
            Set<ConstraintViolation<ValuationDTO>> violations = validator.validate(valuationDTO);
            if (!violations.isEmpty()) {
                String message = violations.stream()
                        .map(violation -> String.format("%s: %s", violation.getPropertyPath(), violation.getMessage()))
                        .collect(Collectors.joining(", "));
                throw new BusinessValidationException("Validación de valorización fallida: " + message);
            }
            valorizacion = valuationMapper.toValuation(valuationDTO);
            valorizacion = valuationRepository.save(valorizacion);
        } catch (DuplicateKeyException e) {
            throw new BusinessValidationException("Error al guardar la valorización: " + e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException("Error inesperado al guardar la valorización: " + e.getMessage());
        }
        return valuationMapper.toValuationDTO(valorizacion);
    }

    @Override
    public List<ValuationDTO> getAllValuations() {
        return valuationRepository.findAll().stream()
                .map(valuationMapper::toValuationDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ValuationDTO> updateValuation(Integer id, ValuationDTO valuationDTO) throws Exception {
        try {
            Set<ConstraintViolation<ValuationDTO>> violations = validator.validate(valuationDTO);
            if (!violations.isEmpty()) {
                String message = violations.stream()
                        .map(violation -> String.format("%s: %s", violation.getPropertyPath(), violation.getMessage()))
                        .collect(Collectors.joining(", "));
                throw new BusinessValidationException("Validación de valorización fallida: " + message);
            }
            Valorizacion valorizacionToUpdate = valuationRepository.findByIdValorizacion(id)
                    .orElseThrow(() -> new EntityNotFoundException("Valorización no encontrada con ID: " + id));
            valorizacionToUpdate.setFecha(valuationDTO.getValuationDate());
            valorizacionToUpdate.setValor(valuationDTO.getValue());
            valorizacionToUpdate.getCliente().setIdCliente(valuationDTO.getClientId());
            valorizacionToUpdate.getNave().setIdNave(valuationDTO.getShipId());
            valorizacionToUpdate.getPuerto().setIdPuerto(valuationDTO.getPortId());
            valorizacionToUpdate.getViaje().setIdViaje(valuationDTO.getTripId());
            valorizacionToUpdate.getContenedor().setIdContenedor(valuationDTO.getContainerId());
            valorizacionToUpdate.getTransporteTerrestre().setIdTransporte(valuationDTO.getTerrestrialTransportId());
            valuationRepository.save(valorizacionToUpdate);
            return Optional.of(valuationMapper.toValuationDTO(valorizacionToUpdate));
        } catch (DuplicateKeyException e) {
            throw new BusinessValidationException("Error al actualizar la valorización: " + e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Error al actualizar la valorización: " + e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException("Error inesperado al actualizar la valorización: " + e.getMessage());
        }
    }

    @Override
    public Optional<ValuationDTO> getValuationById(Integer id) {
        return valuationRepository.findByIdValorizacion(id)
                .map(valuationMapper::toValuationDTO);
    }

    @Override
    @Transactional
    public void deleteValuationById(Integer id) {
        if (!valuationRepository.existsByIdValorizacion(id)) {
            throw new EntityNotFoundException("Valorización no encontrada con ID: " + id);
        }
        valuationRepository.deleteByIdValorizacion(id);
    }

    @Override
    public Boolean existsValuationById(Integer id) {
        return valuationRepository.existsByIdValorizacion(id);
    }

    @Override
    public List<ValuationDTO> getValuationsByClientId(Integer clientId) {
        return valuationRepository.findByClienteIdCliente(clientId).stream()
                .map(valuationMapper::toValuationDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ValuationDTO> getValuationsByShipId(Integer shipId) {
        return valuationRepository.findByNaveIdNave(shipId).stream()
                .map(valuationMapper::toValuationDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ValuationDTO> getValuationsByPortId(Integer portId) {
        return valuationRepository.findByPuertoIdPuerto(portId).stream()
                .map(valuationMapper::toValuationDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ValuationDTO> getValuationsByTripId(Integer tripId) {
        return valuationRepository.findByViajeIdViaje(tripId).stream()
                .map(valuationMapper::toValuationDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ValuationDTO> getValuationsByContainerId(Integer containerId) {
        return valuationRepository.findByContenedorIdContenedor(containerId).stream()
                .map(valuationMapper::toValuationDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ValuationDTO> getValuationsByGroundTransportId(Integer groundTransportId) {
        return valuationRepository.findByTransporteTerrestreIdTransporte(groundTransportId).stream()
                .map(valuationMapper::toValuationDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void exportExcelValuations(HttpServletResponse response) throws Exception {
        List<Valorizacion> valorizaciones = valuationRepository.findAll();
        if (valorizaciones.isEmpty()) {
            throw new EntityNotFoundException("No se encontraron valorizaciones.");
        }

        // Crear el libro de Excel
        Workbook workbook = new XSSFWorkbook();
        var sheet = workbook.createSheet("Valorizaciones");

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

        // Estilo para el subtítulo "Listado de Valorizaciones"
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
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7)); // Combinar 8 celdas para el título

        // Crear subtítulo "Listado de Valorizaciones"
        var subtitleRow = sheet.createRow(1);
        var subtitleCell = subtitleRow.createCell(0);
        subtitleCell.setCellValue("Listado de Valorizaciones");
        subtitleCell.setCellStyle(subtitleStyle);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 7)); // Combinar celdas de la fila del subtítulo

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
                "Fecha", "Nave", "Transporte", "Cliente", "Contenedor", "Origen viaje", "Destino viaje", "Valor $"
        };
        for (int i = 0; i < headers.length; i++) {
            var cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Estilo para la fecha
        CellStyle dateStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy")); // Formato de fecha
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
        for (int i = 0; i < valorizaciones.size(); i++) {
            Valorizacion valorizacion = valorizaciones.get(i);
            var row = sheet.createRow(i + 3); // Comienza desde la fila 3

            // Fecha (formateada)
            var fechaCell = row.createCell(0);
            if (valorizacion.getFecha() != null) {
                fechaCell.setCellValue(valorizacion.getFecha()); // La fecha se formatea automáticamente
                fechaCell.setCellStyle(dateStyle); // Aplicar el estilo de fecha
            } else {
                fechaCell.setCellValue("");
            }

            // Nave
            var naveCell = row.createCell(1);
            naveCell.setCellValue(valorizacion.getNave().getNombreBarco() != null ? valorizacion.getNave().getNombreBarco() : "");
            naveCell.setCellStyle(dataStyle);

            // Transporte
            var transportistaCell = row.createCell(2);
            transportistaCell.setCellValue(valorizacion.getTransporteTerrestre().getNombreTransportista() != null ?
                    valorizacion.getTransporteTerrestre().getNombreTransportista() : "");
            transportistaCell.setCellStyle(dataStyle);

            // Cliente
            var clienteCell = row.createCell(3);
            clienteCell.setCellValue(valorizacion.getCliente().getNombre() != null ? valorizacion.getCliente().getNombre() : "");
            clienteCell.setCellStyle(dataStyle);

            // Contenedor
            var contenedorCell = row.createCell(4);
            contenedorCell.setCellValue(valorizacion.getContenedor().getCodigoContenedor() != null ?
                    valorizacion.getContenedor().getCodigoContenedor() : "");
            contenedorCell.setCellStyle(dataStyle);

            // Origen viaje
            var origenCell = row.createCell(5);
            origenCell.setCellValue(valorizacion.getViaje().getOrigen() != null ? valorizacion.getViaje().getOrigen() : "");
            origenCell.setCellStyle(dataStyle);

            // Destino viaje
            var destinoCell = row.createCell(6);
            destinoCell.setCellValue(valorizacion.getPuerto().getNombre() != null ? valorizacion.getPuerto().getNombre() : "");
            destinoCell.setCellStyle(dataStyle);

            // Valor $
            double valor = Double.parseDouble(valorizacion.getValor()); // Obtener el valor numérico
            String formattedValue = String.format("%,.0f", valor).replace(",", "."); // Formatear con puntos
            var valueCell = row.createCell(7);
            valueCell.setCellValue(formattedValue); // Asignar como texto formateado
            valueCell.setCellStyle(dataStyle);
        }

        // Ajustar ancho de las columnas
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1000); // Aumentar el ancho de las columnas
        }

        // Configurar respuesta HTTP
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=valorizaciones.xlsx");
        try (OutputStream os = response.getOutputStream()) {
            workbook.write(os);
        } finally {
            workbook.close();
        }
    }

    @Override
    public void exportExcelValuationsByClientAndDate(
            HttpServletResponse response,
            Integer clientId,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) throws IOException {
        // Obtener las valorizaciones filtradas por cliente y rango de fechas
        List<Valorizacion> valorizaciones = valuationRepository.findByClienteIdClienteAndFechaBetween(clientId, startDate, endDate);
        if (valorizaciones.isEmpty()) {
            throw new EntityNotFoundException("No se encontraron valorizaciones para el cliente con ID: " + clientId + " en el rango de fechas especificado.");
        }

        // Crear el libro de Excel
        Workbook workbook = new XSSFWorkbook();
        var sheet = workbook.createSheet("ValorizacionesPorCliente");

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

        // Estilo para el subtítulo "Listado de Valorizaciones por Cliente y Fechas"
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
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7)); // Combinar 8 celdas para el título

        // Crear subtítulo "Listado de Valorizaciones por Cliente y Fechas"
        var subtitleRow = sheet.createRow(1);
        var subtitleCell = subtitleRow.createCell(0);
        subtitleCell.setCellValue("Listado de Valorizaciones por Cliente");
        subtitleCell.setCellStyle(subtitleStyle);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 7)); // Combinar celdas de la fila del subtítulo

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
                "Fecha", "Cliente", "Nave", "Contenedor", "Transportista", "Origen del Viaje", "Destino del Viaje", "Valor $"
        };
        for (int i = 0; i < headers.length; i++) {
            var cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Estilo para la fecha
        CellStyle dateStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy")); // Formato de fecha
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

        double total = 0;
        // Agregar contenido
        for (int i = 0; i < valorizaciones.size(); i++) {
            Valorizacion valorizacion = valorizaciones.get(i);
            var row = sheet.createRow(i + 3); // Comienza desde la fila 3

            // Fecha (formateada)
            var fechaCell = row.createCell(0);
            if (valorizacion.getFecha() != null) {
                fechaCell.setCellValue(valorizacion.getFecha()); // La fecha se formatea automáticamente
                fechaCell.setCellStyle(dateStyle); // Aplicar el estilo de fecha
            } else {
                fechaCell.setCellValue("");
            }

            // Cliente
            var clienteCell = row.createCell(1);
            clienteCell.setCellValue(valorizacion.getCliente().getNombre() != null ? valorizacion.getCliente().getNombre() : "");
            clienteCell.setCellStyle(dataStyle);

            // Nave
            var naveCell = row.createCell(2);
            naveCell.setCellValue(valorizacion.getNave().getNombreBarco() != null ? valorizacion.getNave().getNombreBarco() : "");
            naveCell.setCellStyle(dataStyle);

            // Contenedor
            var contenedorCell = row.createCell(3);
            contenedorCell.setCellValue(valorizacion.getContenedor().getCodigoContenedor() != null ? valorizacion.getContenedor().getCodigoContenedor() : "");
            contenedorCell.setCellStyle(dataStyle);

            // Transportista
            var transportistaCell = row.createCell(4);
            transportistaCell.setCellValue(valorizacion.getTransporteTerrestre().getNombreTransportista() != null ?
                    valorizacion.getTransporteTerrestre().getNombreTransportista() : "");
            transportistaCell.setCellStyle(dataStyle);

            // Origen del Viaje
            var origenCell = row.createCell(5);
            origenCell.setCellValue(valorizacion.getViaje().getOrigen() != null ? valorizacion.getViaje().getOrigen() : "");
            origenCell.setCellStyle(dataStyle);

            // Destino del Viaje
            var destinoCell = row.createCell(6);
            destinoCell.setCellValue(valorizacion.getPuerto().getNombre() != null ? valorizacion.getPuerto().getNombre() : "");
            destinoCell.setCellStyle(dataStyle);

            // Valor $
            double valor = Double.parseDouble(valorizacion.getValor()); // Obtener el valor numérico
            String formattedValue = String.format("%,.0f", valor).replace(",", "."); // Formatear con puntos
            var valueCell = row.createCell(7);
            valueCell.setCellValue(formattedValue); // Asignar como texto formateado
            valueCell.setCellStyle(dataStyle);
            total+=valor;
        }
        // Crear estilo para la celda del total
        CellStyle totalStyle = workbook.createCellStyle();
        totalStyle.setAlignment(HorizontalAlignment.CENTER); // Centrar el contenido
        totalStyle.setBorderTop(BorderStyle.THIN); // Borde superior
        totalStyle.setBorderBottom(BorderStyle.THIN); // Borde inferior
        totalStyle.setBorderLeft(BorderStyle.THIN); // Borde izquierdo
        totalStyle.setBorderRight(BorderStyle.THIN); // Borde derecho
        totalStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex()); // Color de fondo celeste claro
        totalStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); // Relleno sólido

// Agregar fila de total
        var totalRow = sheet.createRow(valorizaciones.size() + 3); // Fila después de los datos

// Celda para la etiqueta "TOTAL"
        var totalLabelCell = totalRow.createCell(6);
        totalLabelCell.setCellValue("TOTAL");
        totalLabelCell.setCellStyle(totalStyle); // Aplicar el estilo personalizado

// Celda para el valor total
        var totalValueCell = totalRow.createCell(7);
        totalValueCell.setCellValue(String.format("%,.0f", total).replace(",", ".")); // Formatear el total
        totalValueCell.setCellStyle(totalStyle); // Aplicar el estilo personalizado

        // Ajustar ancho de las columnas
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1000); // Aumentar el ancho de las columnas
        }

        // Configurar respuesta HTTP
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=valorizaciones_cliente_" + clientId + "_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx");

        // Escribir el archivo Excel en la respuesta
        try (OutputStream os = response.getOutputStream()) {
            workbook.write(os);
        } catch (IOException e) {
            throw new IOException("Error al escribir el archivo Excel en la respuesta: " + e.getMessage(), e);
        } finally {
            workbook.close();
        }
    }
}