export class BackendErrors {

  static readonly backendErrorPrefix = 'backendError';

  constructor(private errors: any[]) {

  }

  getFieldErrors(fieldName): string[] {
    console.log(this.errors);
    // return this.errors
    //   .filter(error => error.field === fieldName)
    //   .map(error => `${BackendErrors.backendErrorPrefix}.${fieldName}.${error}`);

    let errors = this.errors
      .filter(error => error.field === fieldName)
      .map(error => `${BackendErrors.backendErrorPrefix}.${fieldName}.${error.code}`);
    console.log(`on return: ${errors}`);
    return errors;
  }
}
