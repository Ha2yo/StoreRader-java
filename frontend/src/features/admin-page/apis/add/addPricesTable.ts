export const addPricesTable = {
  prices: (inspectDay: string) =>
     `/admin/public-data/prices/collect?inspectDay=${inspectDay}`,
} as const;