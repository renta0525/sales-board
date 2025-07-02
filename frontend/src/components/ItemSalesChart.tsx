import {
    PieChart,
    Pie,
    Cell,
    BarChart,
    Bar,
    XAxis,
    YAxis,
    Tooltip,
    Legend,
    ResponsiveContainer,
} from "recharts";
import type { ItemSales } from "../api/salesApi";

const COLORS = ["#8884d8", "#82ca9d", "#ffc658", "#ff7f50", "#a0d911"];

interface Props {
    data: ItemSales[];
    chartType: "pie" | "bar";
};

export const ItemSalesChart = ({ data, chartType }: Props) => {
    return (
        <ResponsiveContainer width="100%" height={300}>
            {chartType === "pie" ? (
                <PieChart>
                    <Pie
                    data={data}
                    dataKey="totalSales"
                    nameKey="itemName"
                    outerRadius={100}
                    label
                    >
                    {data.map((_, index) => (
                        <Cell key={index} fill={COLORS[index % COLORS.length]} />
                    ))}
                    </Pie>
                    <Tooltip />
                    <Legend />
                </PieChart>
            ) : (
                <BarChart data={data}>
                    <XAxis dataKey="itemName" />
                    <YAxis />
                    <Tooltip />
                    <Legend />
                    <Bar dataKey="totalSales" fill="#8884d8" />
                </BarChart>
                )}
            </ResponsiveContainer>
    );
};
