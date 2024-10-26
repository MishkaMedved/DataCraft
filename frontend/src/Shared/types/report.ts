export type report = {
  name: string
  status: "Опубликовано" | "Черновик" | "На рассмотрении"
  createdAt: string
  lastUpdate: string
  isFavorite: boolean
}