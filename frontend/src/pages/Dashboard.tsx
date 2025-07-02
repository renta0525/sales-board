import React, { useEffect, useState } from "react";
import { fetchItemSales, fetchDailySales, fetchUserSales, type ItemSales, type DailySales, type UserSales } from "../api/salesApi";
import { ItemSalesChart } from "../components/ItemSalesChart";
import { SalesBarChart } from "../components/SalesBarChart";
import {
    Box,
    ToggleButton,
    ToggleButtonGroup,
    Tabs,
    Tab,
    Button
} from "@mui/material";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { DatePicker } from "@mui/x-date-pickers/DatePicker";
import { AdapterDateFns } from "@mui/x-date-pickers/AdapterDateFns";
import { ja } from "date-fns/locale";
import { exportItemSalesToCsv } from "../utils/exportToCsv";
import { exportDailySalesToCsv } from "../utils/exportToCsv";
import { exportUserSalesToCsv } from "../utils/exportToCsv";
import { SalesDataTable } from "../components/SalesDataTable";


// dailyDataとuserDataの型調整用
interface ChartData {
    name: string;
    totalSales: number;
}

export const Dashboard = () => {
    const [fromDate, setFromDate] = useState<Date | null>(new Date("2025-06-01"));
    const [toDate, setToDate] = useState<Date | null>(new Date("2025-06-30"));
    const [chartType, setChartType] = useState<"pie" | "bar">("pie");

    type ChartTab = "item" | "daily" | "user";
    const [tab, setTab] = useState<ChartTab>("item");
    const [itemData, setItemData] = useState<ItemSales[]>([]);
    const [dailyData, setDailyData] = useState<ChartData[]>([]);
    const [userData, setUserData] = useState<ChartData[]>([]);
    // CSV出力用
    const [dailyRawData, setDailyRawData] = useState<DailySales[]>([]);
    const [userRawData, setUserRawData] = useState<UserSales[]>([]);

    const handleChartChange = (_: React.MouseEvent<HTMLElement>, newType: "pie" | "bar" | null) => {
        if (newType != null) {
            setChartType(newType);
        }
    };

    useEffect (() => {
        const from = fromDate?.toISOString().slice(0, 10);
        const to = toDate?.toISOString().slice(0, 10);
        if (!from || !to) return;
        if (tab === "item") fetchItemSales(from, to).then(setItemData);
        if (tab === "daily") {
            fetchDailySales(from, to).then((res) => {
                setDailyRawData(res);
                setDailyData(res.map((d) => ({ name: d.date, totalSales: d.totalSales })));
            });
        }
        if (tab === "user") {
            fetchUserSales(from, to).then((res) => {
                setUserRawData(res);
                setUserData(res.map((u) => ({ name: u.userName, totalSales: u.totalSales })));
            });
        }
    }, [fromDate, toDate, tab]);

    return (
        <LocalizationProvider dateAdapter={AdapterDateFns} adapterLocale={ja}>
            <Box sx={{ p: 3 }}>
                <h2>売上ダッシュボード</h2>

                <Box sx={{ display: "flex", gap: 2, mb: 3 }}>
                    <DatePicker
                        label="開始日"
                        value={fromDate}
                        onChange={(newValue) => setFromDate(newValue)}
                        slotProps={{ textField: { size: "small" } }}
                    />
                    <DatePicker
                        label="終了日"
                        value={toDate}
                        onChange={(newValue) => setToDate(newValue)}
                        slotProps={{ textField: { size: "small" } }}
                    />
                </Box>

                <Box sx={{ display: "flex", gap: 3, mb: 2 }}>
                    <Button
                        variant="outlined"
                        onClick={() => {
                            if (tab === "item") exportItemSalesToCsv(itemData);
                            else if (tab === "daily") exportDailySalesToCsv(dailyRawData);
                            else if (tab === "user") exportUserSalesToCsv(userRawData);
                        }}
                        disabled={
                            (tab === "item" && itemData.length === 0) ||
                            (tab === "daily" && dailyData.length === 0) ||
                            (tab === "user" && userData.length === 0)
                        }
                    >
                        CSV出力
                    </Button>
                </Box>

                <Tabs value={tab} onChange={(_, newValue) => setTab(newValue)}>
                    <Tab label="商品別" value="item" />
                    <Tab label="日別" value="daily" />
                    <Tab label="ユーザー別" value="user" />
                </Tabs>

                <ToggleButtonGroup
                    value={chartType}
                    exclusive
                    onChange={handleChartChange}
                    aria-label="chart type"
                    sx={{ mt: 2, mb: 2 }}
                >
                    <ToggleButton value="pie">円グラフ</ToggleButton>
                    <ToggleButton value="bar">棒グラフ</ToggleButton>
                </ToggleButtonGroup>

                {tab === "item" && (
                    <>
                        <ItemSalesChart data={itemData} chartType={chartType} />
                        <SalesDataTable
                            rows={itemData.map((d, i) => ({
                                id: i,
                                name: d.itemName,
                                totalSales: d.totalSales,
                            }))}
                            xLabel="商品名"
                        />
                    </>
                )}

                {tab === "daily" && (
                    <>
                        {chartType === "pie" ? (
                            <ItemSalesChart
                                data={dailyData.map((d) => ({
                                    itemName: d.name,
                                    totalSales: d.totalSales,
                                    totalQuantity: 0,
                                }))}
                                chartType="pie"
                            />
                        ) : (
                            <SalesBarChart data={dailyData} xLabel="日付" />
                        )}
                        <SalesDataTable
                            rows={dailyData.map((d, i) => ({
                                id: i,
                                name: d.name,
                                totalSales: d.totalSales,
                            }))}
                            xLabel="日付"
                        />
                    </>
                )}

                {tab === "user" && (
                    <>
                        {chartType === "pie" ? (
                            <ItemSalesChart
                                data={userData.map((d) => ({
                                    itemName: d.name,
                                    totalSales: d.totalSales,
                                    totalQuantity: 0,
                                }))}
                                chartType="pie"
                            />
                        ) : (
                            <SalesBarChart data={userData} xLabel="ユーザー名" />
                        )}
                        <SalesDataTable
                            rows={userData.map((d, i) => ({
                                id: i,
                                name: d.name,
                                totalSales: d.totalSales,
                            }))}
                            xLabel="ユーザー名"
                        />
                    </>
                )}
            </Box>
        </LocalizationProvider>
    );
};
