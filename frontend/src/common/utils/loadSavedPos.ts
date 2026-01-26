export function loadSavedPosition() {
    const saved = localStorage.getItem("lastPosition");
    if (!saved) return null;

    const pos = JSON.parse(saved);
    return pos;
}