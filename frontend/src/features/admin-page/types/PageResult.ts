export type PageResult<T> = {
  items: T[];
  totalCount: number;
  page: number;
  size: number;
};
