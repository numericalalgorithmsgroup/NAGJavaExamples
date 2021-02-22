import numpy as np
import matplotlib.pyplot as plt
from pathlib import Path

# Set the print precision
np.set_printoptions(precision=4, suppress=True)

# Set the data folder path
data_folder = Path("data")

def read_matrix_file(fname):
    with open(data_folder / fname) as fdata:
        a = np.array([line.split() for line in fdata], dtype = np.float64)
    return a

# Figure 2
diameter = read_matrix_file("diameter.d")[0]
density = read_matrix_file("density.d")[0]

fig = plt.figure()
ax = fig.add_subplot(111)
ax.set_title('PADC etch track diameter histogram', fontsize=16)
ax.set_xlabel('Diameter (nm)')
ax.set_ylabel('Density')
ax.set_xlim(xmin=1, xmax=65)
ax.bar(diameter, density, color='lightsteelblue')
ax.grid()
plt.savefig("./img/fig2.png")
plt.clf()

# Figure 3

dh = read_matrix_file("dh.d")[0]
lopt = read_matrix_file("lopt.d")[0]
gopt = read_matrix_file("gopt.d")[0]
w = read_matrix_file("w.d")[0]

fig = plt.figure()
ax = fig.add_subplot(111)
ax.set_title('PADC etch track diameter histogram and fit', fontsize=16)
ax.set_xlabel('Diameter (nm)')
ax.set_ylabel('Density')
ax.set_xlim(xmin=1, xmax=65)
ax.bar(diameter, density, color='lightsteelblue')
ax.plot(dh, w, '-', linewidth=3, color='tab:green')
ax.grid()
ax.legend(['Aggregated', 'Measured track diameter density'])
plt.savefig("./img/fig3.png")
plt.clf()

# Figure 4
x = read_matrix_file("x.d")[0]
aopt = x[0]
bopt = x[1]
Alopt = x[2]
muopt = x[3]
sigmaopt = x[4]
Agopt = x[5]

fig = plt.figure()
ax = fig.add_subplot(111)
ax.set_title('PADC etch track diameter histogram unfolding', fontsize=16)
ax.set_xlabel('Diameter (nm)')
ax.set_ylabel('Density')
ax.set_xlim(xmin=1, xmax=65)
ax.bar(diameter, density, color='lightsteelblue')
ax.plot(dh, lopt, '-', linewidth=4, color='tab:red')
ax.plot(dh, gopt, '-', linewidth=4, color='tab:blue')
ax.plot(dh, w, '-', linewidth=3, color='tab:green')
ax.grid()
glab = 'Unfolded Normal($\\mu=%1.2f$, $\\sigma=%1.2f, A=%1.2f$)' % (muopt, sigmaopt, Agopt)
llab = 'Unfolded log-Normal($a=%1.2f$, $b=%1.2f, A=%1.2f$)' % (aopt, bopt, Alopt)
ax.legend([llab, glab, 'Aggregated', 'Measured track diameter density'])
plt.savefig("./img/fig4.png")
plt.clf()
