from fastapi import FastAPI
import matplotlib.pyplot as plt
from pydantic import BaseModel
import numpy as np

class Report(BaseModel):
    id: int
    name: str
    status: str
    createdAt: str
    updatedAt: str

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

@app.post("/upload/{id}")
def upload_data(id: int, json_data):
    

@app.post("/")
def get_diagram_image(report: Report):
    return {"filename": f"{str(data.id)}.png"}

def generate_circle_diagram(report: Report):
    
    fig, ax = plt.subplots()
    ax.pie(data.sizes, labels=data.labels)
    plt.title(data.title)
    plt.savefig(f"{str(data.id)}.png")

def generate_bar_diagram(report: Report):
    plt.bar(data.labels, data.counts)
    plt.title(data.title)
    plt.savefig(f"{str(data.id)}.png")

def generate_line_diagram(report: Report):
    plt.plot(data.x_array, data.y_array)
    plt.xlabel(data.x_title)
    plt.ylabel(data.y_title)
    plt.title(data.title)
    plt.savefig(f"{str(data.id)}.png")