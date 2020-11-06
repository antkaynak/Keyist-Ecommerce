const options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric', hour: 'numeric', minute: 'numeric' };

export function getLocaleDate(dateInMilliseconds: number): string {
  return new Date(dateInMilliseconds).toLocaleDateString(undefined, options);
}
