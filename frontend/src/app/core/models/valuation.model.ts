export interface Valuation {
  valuationId?: number;
  valuationDate: string;
  value: string;
  clientId: number;
  shipId: number;
  portId: number;
  tripId: number;
  containerId: number;
  terrestrialTransportId: number;
  clientName?: string;
  shipName?: string;
  portName?: string;
  tripCode?: string;
  containerCode?: string;
  transportName?: string;
}
