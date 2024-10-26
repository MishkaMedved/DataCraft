import { createColumnHelper, flexRender, getCoreRowModel, useReactTable, } from '@tanstack/react-table'
import { useState } from 'react'
import { report } from '../../Shared/types/report'
import { FavoriteButton } from '../../Shared/ui/FavoriteButton'

const defaultData: report[] = [
  {
    name: "Дашборд специалиста по контекстной рекламе",
    status: "Опубликовано",
    createdAt: "26-10-2024",
    lastUpdate: "26-10-2024",
    isFavorite: true
  },
  {
    name: "Дашборд специалиста по контекстной рекламе",
    status: "Черновик",
    createdAt: "26-10-2024",
    lastUpdate: "26-10-2024",
    isFavorite: true
  },
  {
    name: "Дашборд специалиста по контекстной рекламе",
    status: "Опубликовано",
    createdAt: "26-10-2024",
    lastUpdate: "26-10-2024",
    isFavorite: true
  },
  {
    name: "Дашборд специалиста по контекстной рекламе",
    status: "Опубликовано",
    createdAt: "26-10-2024",
    lastUpdate: "26-10-2024",
    isFavorite: true
  },
  {
    name: "Дашборд специалиста по контекстной рекламе",
    status: "Опубликовано",
    createdAt: "26-10-2024",
    lastUpdate: "26-10-2024",
    isFavorite: false
  },
]

const columnHelper = createColumnHelper<report>()

const columns = [
  columnHelper.accessor("name", {
    header: () => <span>Название</span>,
    cell: info => info.getValue(),
  }),
  columnHelper.accessor("status", {
    header: () => <span>Статус</span>,
    cell: info => info.getValue(),
  }),
  columnHelper.accessor("createdAt", {
    header: () => <span>Создан</span>,
    cell: info => info.getValue(),
  }),
  columnHelper.accessor("lastUpdate", {
    header: () => <span>Изменён</span>,
    cell: info => info.getValue(),
  }),
  columnHelper.accessor("isFavorite", {
    header: () => <span>Избранное</span>,
    cell: info => <FavoriteButton isFavorite={info.getValue()} />
  })
]

export function ReportTable() {
  const [data, setData] = useState<report[]>(() => [...defaultData])

  const table = useReactTable({
    data,
    columns,
    getCoreRowModel: getCoreRowModel()
  })

  return (
    <table>
      <thead>
        {
          table.getHeaderGroups().map((headerGroups) => (
            <tr key={headerGroups.id}>
              {
                headerGroups.headers.map((header) => (
                  <th key={header.id} colSpan={header.colSpan}>
                    {
                      header.isPlaceholder
                        ? null
                        : flexRender(
                          header.column.columnDef.header,
                          header.getContext()
                        )
                    }
                  </th>
                ))
              }
            </tr>
          ))
        }
      </thead>
      <tbody>
        {
          table.getRowModel().rows.map((row) => (
            <tr key={row.id}>
              {row.getVisibleCells().map((cell) => (
                <td key={cell.id}>
                  {flexRender(cell.column.columnDef.cell, cell.getContext())}
                </td>
              ))}
            </tr>
          ))
        }
      </tbody>
    </table>
  )
}
