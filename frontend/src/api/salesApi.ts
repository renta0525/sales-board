import axios from "axios";

export interface ItemSales {
    itemName: string;
    totalQuantity: number;
    totalSales: number;
}

export interface DailySales {
    date: string;
    totalSales: number;
}

export interface UserSales {
    userName: string;
    totalSales: number;
}

export const fetchItemSales = async (from: string, to: string): Promise<ItemSales[]> => {
    const res = await axios.get<ItemSales[]>(`/api/sales/by-item`, {params: { from, to }});
    return res.data;
};

export const fetchDailySales = async (from: string,to: string): Promise<DailySales[]> => {
    const res = await axios.get<DailySales[]>(`/api/sales/by-daily`, { params: { from, to }});
    return res.data;
};

export const fetchUserSales = async (from: string, to: string): Promise<UserSales[]> => {
    const res = await axios.get<UserSales[]>(`/api/sales/by-user`, { params: { from, to }});
    return res.data;
};
