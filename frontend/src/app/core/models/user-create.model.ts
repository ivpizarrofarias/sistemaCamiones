export type UserRole = 'ADMINISTRADOR' | 'SUPERADMINISTRADOR'|'SUPERVISOR';
export interface User {
  firstName: string;
  paternalLastName: string;
  maternalLastName: string;
  email: string;
  password: string;
  userRole: UserRole;
}
