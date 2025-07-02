import { DataGrid } from "@mui/x-data-grid";
import type { GridColDef } from "@mui/x-data-grid";


type Row = {
    id: number;
    name: string;
    totalSales: number;
};

type Props = {
    rows: Row[];
    xLabel: string;
};

export const SalesDataTable = ({ rows, xLabel }: Props) => {
    const columns: GridColDef[] = [
        { field: "name", headerName: xLabel, flex: 1 },
        { field: "totalSales", headerName: "売上合計", flex: 1 },
    ];

    return (
        <div style={{ height: 300, width: "100%", marginTop: "1rem" }}>
            <DataGrid rows={rows} columns={columns} pageSizeOptions={[5, 10]} />
        </div>
    );
};
