export interface StoreDataProps {
    map: L.Map | null;
    storeMarkersRef: React.RefObject<Record<string, L.Marker>>;
    renderKey: number;
}