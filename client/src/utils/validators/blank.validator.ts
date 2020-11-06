import { FormGroup } from '@angular/forms';

export function checkIfBlankValidator(control: FormGroup): { [s: string]: boolean } {
  if (control.value !== null && control.value.trim() !== control.value && control.value.trim() === '') {
    return { blank: true };
  }
  return null;
}

export function notBlankValidator(control: FormGroup): { [s: string]: boolean } {
  // checks if the field is null or has an empty value in it.
  if (control.value === null || control.value.trim() === '') {
    return { blank: true };
  }
  return null;
}
