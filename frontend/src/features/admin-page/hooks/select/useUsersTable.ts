import type { User, UserSortKey } from "../../types/SelectUsers";
import { useServerTable } from "./useServerTable";
import { selectUsersTable } from "../../apis/select/selectUsersTable";

export default function useGoodsTable() {
 return useServerTable<User, UserSortKey>({
  fetcher: selectUsersTable,
  size: 10,
  initialSortKey: "id",
  initialSortOrder: "asc"
 });
}
