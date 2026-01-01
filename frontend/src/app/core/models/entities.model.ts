export interface User {
  userId?: number;
  firstName: string;
  paternalLastName: string;
  maternalLastName: string;
  email: string;
  password?: string;
  userRole?: string;
  creationDate?: string;
}

export interface Trip {
  tripId?: number;
  origin: string;
}

export interface Ship {
  shipId?: number;
  shipName: string;
  voyageNumber: string;
  shippingLine: string;
}

export interface Port {
  portId?: number;
  portName: string;
}

export interface Movement {
  movementId?: number;
  emissionDate: string;
  documentNumber: string;
  documentType: string;
  physicalState: string;
  sizeType: string;
  entryDateTime: string;
  exitDateTime: string;
  truckId: number;
  driverId: number;
  clientId: number;
  shipId: number;
  portId: number;
  terrestrialTransportId: number;
  containerId: number;
  // Campos para visualización
  truckLicensePlate?: string;
  driverName?: string;
  clientName?: string;
  shipName?: string;
  portName?: string;
  transportName?: string;
  containerCode?: string;
}

export interface GroundTransport {
  transportId?: number;
  transporterName: string;
}

export interface Container {
  containerId?: number;
  containerCode: string;
}

export interface Client {
  clientId?: number;
  clientName: string;
  clientEmail: string;
}
