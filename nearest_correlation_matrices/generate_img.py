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

def read_data_file(fname):
    with open(data_folder / fname) as fdata:
        for i, line in enumerate(fdata):
            if i == 0:
                itr = int(line)
            elif i == 1:
                norm = float(line)
            elif i == 2:
                a = np.array([float(lts) for lts in line.split()], dtype = np.float64)
            else:
                a = np.vstack([a, [float(lts) for lts in line.split()]])
    return itr, norm, a

# G02AA
itr, norm, X_G = read_data_file("G02AA.d")

# Plot the difference between G and X as a small shaded square for each element
fig1, ax1 = plt.subplots(figsize=(14, 7))
cax1 = ax1.imshow(X_G, interpolation='none', cmap=plt.cm.Blues, 
                  vmin=0, vmax=0.2)
cbar = fig1.colorbar(cax1, ticks = np.linspace(0.0, 0.2, 11, endpoint=True), 
                     boundaries=np.linspace(0.0, 0.2, 11, endpoint=True))
cbar.mappable.set_clim([0, 0.2])
ax1.tick_params(axis='both', which='both', 
                bottom='off', top='off', left='off', right='off', 
                labelbottom='off', labelleft='off')
ax1.set_title(r'$|G-X|$ for G02AA', fontsize=16)
plt.xlabel(
    r'Iterations: {0}, $||G-X||_F = {1:.4f}$'.format(itr, norm),
    fontsize=14,
)
plt.savefig("./img/G02AA.png")

# G02AB
itr, norm, X_G = read_data_file("G02AB.d")

fig1, ax1 = plt.subplots(figsize=(14, 7))
cax1 = ax1.imshow(X_G, interpolation='none', cmap=plt.cm.Blues, vmin=0, 
                  vmax=0.2)
cbar = fig1.colorbar(cax1, ticks = np.linspace(0.0, 0.2, 11, endpoint=True), 
                     boundaries=np.linspace(0.0, 0.2, 11, endpoint=True))
cbar.mappable.set_clim([0, 0.2])
ax1.tick_params(axis='both', which='both', 
                bottom='off', top='off', left='off', right='off', 
                labelbottom='off', labelleft='off')

ax1.set_title(r'$|G-X|$ for G02AB', fontsize=16)
plt.xlabel(
    r'Iterations: {0}, $||G-X||_F = {1:.4f}$'.format(itr, norm),
    fontsize=14,
)                                         
plt.savefig("./img/G02AB.png")

# G02AJ
itr, norm, X_G = read_data_file("G02AJ.d")

fig1, ax1 = plt.subplots(figsize=(14, 7))
cax1 = ax1.imshow(X_G, interpolation='none', cmap=plt.cm.Blues, vmin=0, 
                  vmax=0.2)
cbar = fig1.colorbar(cax1, ticks = np.linspace(0.0, 0.2, 11, endpoint=True), 
                     boundaries=np.linspace(0.0, 0.2, 11, endpoint=True))
cbar.mappable.set_clim([0, 0.2])
ax1.tick_params(axis='both', which='both', 
                bottom='off', top='off', left='off', right='off', 
                labelbottom='off', labelleft='off')

ax1.set_title(r'$|G-X|$ for G02AJ', fontsize=16)
plt.xlabel(
    r'Iterations: {0}, $||G-X||_F = {1:.4f}$'.format(itr, norm),
    fontsize=14,
)        
plt.savefig("./img/G02AJ.png")

# G02AN
itr, norm, X_G = read_data_file("G02AN.d")

fig1, ax1 = plt.subplots(figsize=(14, 7))
cax1 = ax1.imshow(X_G, interpolation='none', cmap=plt.cm.Blues, vmin=0, 
                  vmax=0.2)
cbar = fig1.colorbar(cax1, ticks = np.linspace(0.0, 0.2, 11, endpoint=True),
                     boundaries=np.linspace(0.0, 0.2, 11, endpoint=True))
cbar.mappable.set_clim([0, 0.2])
ax1.tick_params(axis='both', which='both', 
                bottom='off', top='off', left='off', right='off', 
                labelbottom='off', labelleft='off')

ax1.set_title(r'$|G-X|$ for G02AN', fontsize=16)
plt.xlabel(
    r'Iterations: {0}, $||G-X||_F = {1:.4f}$'.format(itr, norm),
    fontsize=14,
)        
plt.savefig("./img/G02AN.png")
