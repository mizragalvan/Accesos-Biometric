export class Puesto {
  public idPosition: number;
  public name: string;
  public status: string;
  public numberPage: number;
  public totalRows: number;
  
  constructor() {
      this.idPosition = 0;
      this.name = "";
      this.status = "";
      this.numberPage = 0;
      this.totalRows = 0;
  }
};