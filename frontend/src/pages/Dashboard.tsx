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
    Button,
    Typography,
    Paper,
    Container
} from "@mui/material";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { DatePicker } from "@mui/x-date-pickers/DatePicker";
import { AdapterDateFns } from "@mui/x-date-pickers/AdapterDateFns";
import { ja } from "date-fns/locale";
import { exportItemSalesToCsv, exportDailySalesToCsv, exportUserSalesToCsv } from "../utils/exportToCsv";
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

    useEffect(() => {
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

    const commonPaperStyle = {
        p: 3,
        borderRadius: 4,
        boxShadow: "0 8px 32px 0 rgba(31, 38, 135, 0.1)",
        backdropFilter: "blur(4px)",
        backgroundColor: "rgba(255, 255, 255, 0.5)",
        border: "1px solid rgba(255, 255, 255, 0.18)",
        mb: 3
    };

    return (
        <LocalizationProvider dateAdapter={AdapterDateFns} adapterLocale={ja}>
            <Box sx={{ backgroundColor: "#f0f2f5", minHeight: "100vh", display: "flex", alignItems: "center" }}>
                <Container maxWidth="md" sx={{ py: 4 }}>
                    <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center", mb: 3 }}>
                        <Typography variant="h4" component="h1" gutterBottom sx={{ fontWeight: "bold", color: "#333" }}>
                            売上ダッシュボード
                        </Typography>
                        
                    </Box>

                    <Paper sx={commonPaperStyle}>
                        <Box sx={{ display: "flex", gap: 2, alignItems: "center", flexWrap: "wrap" }}>
                            <DatePicker
                                label="開始日"
                                value={fromDate}
                                onChange={(newValue) => setFromDate(newValue)}
                            />
                            <DatePicker
                                label="終了日"
                                value={toDate}
                                onChange={(newValue) => setToDate(newValue)}
                            />
                            <Button
                                variant="contained"
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
                                sx={{ borderRadius: 2, boxShadow: "none", textTransform: "none", height: "56px" }}
                            >
                                CSV出力
                            </Button>
                        </Box>
                    </Paper>

                    <Paper sx={commonPaperStyle}>
                        <Box sx={{ borderBottom: 1, borderColor: 'divider', mb: 2 }}>
                            <Tabs value={tab} onChange={(_, newValue) => setTab(newValue)}
                                sx={{
                                    "& .MuiTabs-indicator": { display: "none" },
                                    "& .MuiTab-root": {
                                        borderRadius: "8px 8px 0 0",
                                        mr: 1,
                                        "&.Mui-selected": {
                                            backgroundColor: "rgba(0, 122, 255, 0.1)",
                                            color: "#007aff",
                                            fontWeight: "bold"
                                        },
                                    },
                                }}
                            >
                                <Tab label="商品別" value="item" />
                                <Tab label="日別" value="daily" />
                                <Tab label="ユーザー別" value="user" />
                            </Tabs>
                        </Box>

                        <ToggleButtonGroup
                            value={chartType}
                            exclusive
                            onChange={handleChartChange}
                            aria-label="chart type"
                            sx={{ mb: 3 }}
                        >
                            <ToggleButton value="pie" sx={{ borderRadius: "16px 0 0 16px" }}>円グラフ</ToggleButton>
                            <ToggleButton value="bar" sx={{ borderRadius: "0 16px 16px 0" }}>棒グラフ</ToggleButton>
                        </ToggleButtonGroup>

                        {tab === "item" && (
                            <>
                                <ItemSalesChart data={itemData} chartType={chartType} />
                                <SalesDataTable
                                    rows={itemData.map((d, i) => ({ id: i, name: d.itemName, totalSales: d.totalSales }))}
                                    xLabel="商品名"
                                />
                            </>
                        )}

                        {tab === "daily" && (
                            <>
                                {chartType === "pie" ? (
                                    <ItemSalesChart data={dailyData.map((d) => ({ itemName: d.name, totalSales: d.totalSales, totalQuantity: 0 }))} chartType="pie" />
                                ) : (
                                    <SalesBarChart data={dailyData} xLabel="日付" />
                                )}
                                <SalesDataTable
                                    rows={dailyData.map((d, i) => ({ id: i, name: d.name, totalSales: d.totalSales }))}
                                    xLabel="日付"
                                />
                            </>
                        )}

                        {tab === "user" && (
                            <>
                                {chartType === "pie" ? (
                                    <ItemSalesChart data={userData.map((d) => ({ itemName: d.name, totalSales: d.totalSales, totalQuantity: 0 }))} chartType="pie" />
                                ) : (
                                    <SalesBarChart data={userData} xLabel="ユーザー名" />
                                )}
                                <SalesDataTable
                                    rows={userData.map((d, i) => ({ id: i, name: d.name, totalSales: d.totalSales }))}
                                    xLabel="ユーザー名"
                                />
                            </>
                        )}
                    </Paper>
                </Container>
            </Box>
        </LocalizationProvider>
    );
};
