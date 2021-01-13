import csv
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

data_file = "djia_close_price.csv"

with open(data_folder / data_file) as fdata:
    csv_reader = csv.reader(fdata, delimiter=',')
    line_count = 0
    close_price = {}
    for row in csv_reader:
        if line_count == 0:
            date_index = row[1:]
            line_count += 1
        else:
            close_price[row[0]] = np.array(row[1:], dtype=np.float64)

# Size of data, m: number of observations, n: number of stocks
m = len(date_index)
n = len(close_price)

# Extract stock closing prices to a numpy array
data = np.zeros(shape=(m, n))
i = 0
for stock in close_price:
    data[:,i] = close_price[stock]
    plt.plot(np.arange(m), data[:,i])
    i += 1
# Plot closing prices
plt.xlabel('Time (days)')
plt.ylabel('Closing price ($)')
plt.savefig("./img/data.png")
plt.clf()

# Relative return
rel_rtn = read_matrix_file("relRtn.d")

# Plot relative return
for i in range(n):
    plt.plot(np.arange(m-1),rel_rtn[:,i])
plt.xlabel('Time (days)')
plt.ylabel('Relative return')
plt.savefig("./img/rel_rtn.png")
plt.clf()

# Efficient Frontier
ab_risk = read_matrix_file("abRisk.d")
ab_rtn = read_matrix_file("abRtn.d")

plt.plot(ab_risk*100.0, ab_rtn*100.0)
plt.ylabel('Total Expected Return (%)')
plt.xlabel('Absolute Risk (%)')
plt.savefig("./img/risk_1.png")
plt.clf()

# Maximizing the Sharpe ratio
sr = read_matrix_file("sr.d")
sr_risk = sr[0][0]
sr_rtn = sr[1][0]

plt.plot(ab_risk*100.0, ab_rtn*100.0, label='Efficient frontier')
plt.plot([sr_risk*100], [sr_rtn*100], 'rs', label='Portfolio with maximum Sharpe ratio')
plt.plot([sr_risk*100, 0.0], [sr_rtn*100, 0.0], 'r-', label='Capital market line')
plt.axis([min(ab_risk*100), max(ab_risk*100), min(ab_rtn*100), max(ab_rtn*100)])
plt.ylabel('Total Expected Return (%)')
plt.xlabel('Absolute Risk (%)')
plt.legend()
plt.savefig("./img/risk_2.png")
plt.clf()

# Portfolio optimization with tracking-error constraint
b = read_matrix_file("b.d")
b_risk = b[0][0]
b_rtn = b[1][0]
tev_risk = read_matrix_file("tevRisk.d")
tev_rtn = read_matrix_file("tevRtn.d")

plt.figure(figsize=(7.5, 5.5))
plt.plot(ab_risk*100.0, ab_rtn*100.0, label='Classic efficient frontier')
plt.plot([sr_risk*100], [sr_rtn*100], 'rs', label='Portfolio with maximum Sharpe ratio')
plt.plot([sr_risk*100, 0.0], [sr_rtn*100, 0.0], 'r-', label='Capital market line')
plt.plot(b_risk*100, b_rtn*100, 'r*', label='Benchmark portfolio')
plt.plot(tev_risk*100.0, tev_rtn*100.0, 'seagreen', label='Efficient frontier with tev constraint')

plt.axis([min(ab_risk*100), max(ab_risk*100), min(tev_rtn*100), max(ab_rtn*100)])
plt.ylabel('Total Expected Return (%)')
plt.xlabel('Absolute Risk (%)')
plt.legend()
plt.savefig("./img/risk_3.png")
plt.clf()
