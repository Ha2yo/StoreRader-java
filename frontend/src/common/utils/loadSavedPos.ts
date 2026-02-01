/**
 * File: common/utils/loadSavedPos.ts
 * Description:
 *   로컬 스토리지에 저장된 마지막 위치 정보를 불러온다
 */

export function loadSavedPosition() {
    const saved = localStorage.getItem("lastPosition");
    if (!saved) return null;

    const pos = JSON.parse(saved);
    return pos;
}