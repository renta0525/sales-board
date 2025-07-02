import {
    BarChart,
    Bar,
    XAxis,
    YAxis,
    Tooltip,
    Legend,
    ResponsiveContainer,
} from "recharts";

type Props = {
    data: { name: string; totalSales: number} [];
    xLabel: string;
};

export const SalesBarChart = ({ data, xLabel }: Props) => (
    <ResponsiveContainer width="100%" height={300}>
        <BarChart data={data}>
            <XAxis dataKey="name" label={{ value: xLabel, position: "insideBottom"}} />
            <YAxis />
            <Tooltip />
            <Legend />
            <Bar dataKey="totalSales" fill= "#82ca9d" />
        </BarChart>
    </ResponsiveContainer>
);
