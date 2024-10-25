from fastapi import FastAPI
import matplotlib.pyplot as plt
from pydantic import BaseModel
import numpy as np

class DataForCreatingDiagram(BaseModel):
    id: int
    title: str

class DataForCreatingCircleDiagram(DataForCreatingDiagram):
    sizes: list[int]
    labels: list[str]

class DataForCreatingBarDiagram(DataForCreatingDiagram):
    labels: list[str]
    counts: list[int]

class DataForCreatingLineDiagram(DataForCreatingDiagram):
    x_array: np.array[int]
    y_array: np.array[int]
    x_title: str
    y_title: str

app = FastAPI()

app.get("/")
def get_diagram_image(data: DataForCreatingDiagram):
    match type(data):
        case DataForCreatingCircleDiagram:
            generate_circle_diagram(data)

        case DataForCreatingBarDiagram:
            generate_bar_diagram(data)

        case DataForCreatingLineDiagram:
            generate_line_diagram(data)

    return {"filename": f"{str(data.id)}.png"}

def generate_circle_diagram(data: DataForCreatingCircleDiagram):
    fig, ax = plt.subplots()
    ax.pie(data.sizes, labels=data.labels)
    plt.title(data.title)
    plt.savefig(f"{str(data.id)}.png")

def generate_bar_diagram(data: DataForCreatingBarDiagram):
    plt.bar(data.labels, data.counts)
    plt.title(data.title)
    plt.savefig(f"{str(data.id)}.png")

def generate_line_diagram(data):
    plt.plot(data.x_array, data.y_array)
    plt.xlabel(data.x_title)
    plt.ylabel(data.y_title)
    plt.title(data.title)
    plt.savefig(f"{str(data.id)}.png")