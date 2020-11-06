import { FormGroup } from '@angular/forms';

export function passwordMatchCheckValidator(control: FormGroup): { [s: string]: boolean } {
  if (control.value.newPassword !== control.value.newPasswordConfirm) {
    return { noMatch: true };
  }
  return null;
}
