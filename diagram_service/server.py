from fastapi import FastAPI
import matplotlib.pyplot as plt
from pydantic import BaseModel
import numpy as np

import json

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

@app.post("/{}")
def create_diagram(json_data, critery, diagram_type):
    data = json.load(json_data)

    diagram_data = list()

    for report in data:
        for k, v in report:
            if k==critery:
                diagram_data.append(v)

    if diagram_type=="pie":
        generate_circle_diagram()

def generate_circle_diagram(title, labels, sizes):
    
    fig, ax = plt.subplots()
    ax.pie(sizes, labels=labels)
    plt.title(title)
    plt.savefig(f"{str(data.id)}.png")

def generate_bar_diagram(title, labels, sizes):
    plt.bar(labels, sizes)
    plt.title(title)
    plt.savefig(f"{str(data.id)}.png")

def generate_line_diagram(title, x_array, y_array, x_title, y_title):
    plt.plot(x_array, y_array)
    plt.xlabel(x_title)
    plt.ylabel(y_title)
    plt.title(title)
    plt.savefig(f"{str(data.id)}.png")