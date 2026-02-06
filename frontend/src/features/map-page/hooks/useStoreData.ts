import { useEffect } from "react";
import L from "leaflet";
import { findAllStore } from "../apis/findAllStore";
import type { StoreDataProps } from "../types/StoreDataProps";
import { blackIcon } from "../utils/makerIcon";

export function useStoreData({
    map,
    storeMarkersRef,
    renderKey
}: StoreDataProps) {
    useEffect(() => {
        if (!map) return;

        (async () => {
            
            const stores = await findAllStore();

            Object.values(storeMarkersRef.current).forEach((m) => map.removeLayer(m));
            storeMarkersRef.current = {};

            stores.forEach((store) => {
                
                const lat = store.lat;
                const lng = store.lng;
                console.log("marker candidate:", store.storeId, store.lat, store.lng);

                const marker = L.marker([lat, lng], { icon: blackIcon }).addTo(map);
                storeMarkersRef.current[String(store.storeId)] = marker;

            });
        }) ();
    }, [map, renderKey]);
}