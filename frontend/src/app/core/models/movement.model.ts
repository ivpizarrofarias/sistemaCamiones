export interface Movement {
  movementId?: number;

  // Datos del documento
  emissionDate: string;
  documentNumber: string;
  documentType: string;

  // Estado físico / contenedor
  physicalState: string;
  sizeType: string;

  // Fechas
  entryDateTime: string;
  exitDateTime: string;

  // Relaciones (IDs) → SE ENVÍAN AL BACKEND
  truckId: number;
  driverId: number;
  clientId: number;
  shipId: number;
  portId: number;
  terrestrialTransportId: number;
  containerId: number;

  // 🔹 CAMPOS SOLO PARA VISUALIZACIÓN (NO SE GUARDAN)
  truckLicensePlate?: string;
  driverName?: string;
  clientName?: string;
  shipName?: string;
  portName?: string;
  transportName?: string;
  containerCode?: string;
}
