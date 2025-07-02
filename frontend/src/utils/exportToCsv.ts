import { saveAs } from "file-saver";
import Papa from "papaparse";
import type { DailySales, ItemSales, UserSales } from "../api/salesApi";

export const exportItemSalesToCsv = (data: ItemSales[]) => {
    const csv = Papa.unparse(
        data.map((item) => ({
            商品名: item.itemName,
            売上数量: item.totalQuantity,
            売上合計: item.totalSales,
        }))
    );

    const blob = new Blob([csv], { type: "text/csv;charset=utf-8;" });
    saveAs(blob, "item-sales.csv");
};

export const exportDailySalesToCsv = (data: DailySales[]) => {
    const csv = Papa.unparse(
        data.map((daily) => ({
            日付: daily.date,
            売上合計: daily.totalSales
        }))
    );
    const blob = new Blob([csv], { type: "text/csv;charset=utf-8;" });
    saveAs(blob, "daily-sales.csv");
}

export const exportUserSalesToCsv = (data: UserSales[]) => {
    const csv = Papa.unparse(
        data.map((user) => ({
            ユーザー: user.userName,
            売上合計: user.totalSales,
        }))
    );
    const blob = new Blob([csv], { type: "text/csv;charset=utf-8;" });
    saveAs(blob, "user-sales.csv");
};
