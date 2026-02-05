export const addPricesTable = {
  prices: (inspectDay: string) =>
     `/admin/get/public-data/prices?inspectDay=${inspectDay}`,
} as const;