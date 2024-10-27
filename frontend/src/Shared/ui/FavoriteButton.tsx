type props = {
  isFavorite: boolean
}

export function FavoriteButton({ isFavorite }: props) {
  return (
    <div>{isFavorite ? "true" : "false"}</div>
  )
}
