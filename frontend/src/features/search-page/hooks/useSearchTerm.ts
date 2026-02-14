import { useEffect, useState } from "react";

export function useSearchTerm() {
      const [searchTerm, setSearchTerm] = useState("");

      // 앱 재진입 시 마지막 검색어 복원
      useEffect(() => {
        const savedTerm = localStorage.getItem("lastSearchTerm");
        if (savedTerm) {
          setSearchTerm(savedTerm);
        }
      }, []);
    
      // 검색어 변경 시 로컬스토리지 업데이트
      useEffect(() => {
        if (searchTerm.trim() === "") {
          localStorage.removeItem("lastSearchTerm");
          localStorage.removeItem("selectedGoodName");
          localStorage.removeItem("selectedGoodId")
        } else {
          localStorage.setItem("lastSearchTerm", searchTerm);
        }
      }, [searchTerm]);

      return { searchTerm, setSearchTerm };
}