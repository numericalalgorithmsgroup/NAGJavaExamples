import csv
import numpy as np
import matplotlib.pyplot as plt
from pathlib import Path
from mpl_toolkits.mplot3d import Axes3D
from matplotlib import cm

# Set the print precision
np.set_printoptions(precision=4, suppress=True)

# Set the data folder path
data_folder = Path("data")

def read_matrix_file(fname):
    with open(data_folder / fname) as fdata:
        a = np.array([line.split() for line in fdata], dtype = np.float64)
    return a

# Data
box = read_matrix_file("box.d")
bl = read_matrix_file("bl.d").transpose().flatten()
bu = read_matrix_file("bu.d").transpose().flatten()
steps0 = read_matrix_file("steps[0].d").transpose().flatten()
steps1 = read_matrix_file("steps[1].d").transpose().flatten()
steps2 = read_matrix_file("steps[2].d").transpose().flatten()
X = read_matrix_file("X.d")
Y = read_matrix_file("Y.d")
z_box = read_matrix_file("z_box.d").transpose().flatten()
z_m = read_matrix_file("z_m.d")

# Plot
fig = plt.figure()
ax = Axes3D(fig)
ax = fig.gca(projection='3d')
ax.grid(False)
ax.plot(box[0], box[1], z_box, 'k-', linewidth=1.5)
ax.plot([bl[0], bu[0], bu[0], bl[0], bl[0]], [bl[1], bl[1], bu[1], bu[1], bl[1]], -1.2*np.ones(5), 'k-')
ax.contour(X, Y, z_m, 15, offset=-1.2, cmap=cm.jet)
ax.plot_surface(X, Y, z_m, cmap=cm.jet, alpha=0.5)
ax.set_title('Rosenbrock Function')
ax.set_xlabel(r'$\mathit{x}$')
ax.set_ylabel(r'$\mathit{y}$')
ax.plot(steps0, steps1, steps2, 'o-', color='red', markersize=3, linewidth=2)
ax.azim = 160
ax.elev = 35
plt.savefig("./img/plot.png")
plt.clf()
