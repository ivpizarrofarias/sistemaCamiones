export type UserRole = 'ADMINISTRADOR' | 'SUPERADMINISTRADOR';
export interface User {
  firstName: string;
  paternalLastName: string;
  maternalLastName: string;
  email: string;
  password: string;
  userRole: UserRole;
}
