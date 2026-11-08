import csv
from pathlib import Path

rows = list(csv.DictReader(Path('results/results.csv').read_text(encoding='utf-8-sig').splitlines()))
colors = ['#2864a0', '#d26232', '#458552']

def plot(filename, title, group, case, names):
    data = [[r for r in rows if r['group'] == group and r['case'] == case and r['algorithm'] == name] for name in names]
    sizes = [int(r['size']) for r in data[0]]
    maximum = max(float(r['average_ms']) for series in data for r in series) * 1.15
    width, height = 720, 420
    left, right, top, bottom = 80, 30, 55, 70
    x = lambda v: left + (v - min(sizes)) / (max(sizes) - min(sizes)) * (width - left - right)
    y = lambda v: height - bottom - v / maximum * (height - top - bottom)
    parts = [f'<svg xmlns="http://www.w3.org/2000/svg" width="{width}" height="{height}" viewBox="0 0 {width} {height}">', '<rect width="100%" height="100%" fill="white"/>', f'<text x="360" y="28" text-anchor="middle" font-family="sans-serif" font-size="18">{title}</text>']
    for i in range(5):
        value = maximum * i / 4
        yy = y(value)
        parts.append(f'<line x1="{left}" y1="{yy}" x2="{width-right}" y2="{yy}" stroke="#ddd"/><text x="{left-10}" y="{yy+4}" text-anchor="end" font-family="sans-serif" font-size="12">{value:.2f}</text>')
    for size in sizes:
        parts.append(f'<text x="{x(size)}" y="{height-bottom+20}" text-anchor="middle" font-family="sans-serif" font-size="12">{size}</text>')
    parts.append(f'<line x1="{left}" y1="{top}" x2="{left}" y2="{height-bottom}" stroke="black"/><line x1="{left}" y1="{height-bottom}" x2="{width-right}" y2="{height-bottom}" stroke="black"/>')
    parts.append(f'<text x="{width/2}" y="{height-10}" text-anchor="middle" font-family="sans-serif" font-size="13">Input length</text><text x="18" y="{height/2}" transform="rotate(-90 18 {height/2})" text-anchor="middle" font-family="sans-serif" font-size="13">Average time (ms)</text>')
    for index, series in enumerate(data):
        points = ' '.join(f'{x(int(r["size"]))},{y(float(r["average_ms"]))}' for r in series)
        parts.append(f'<polyline points="{points}" fill="none" stroke="{colors[index]}" stroke-width="2"/>')
        for r in series:
            parts.append(f'<circle cx="{x(int(r["size"]))}" cy="{y(float(r["average_ms"]))}" r="4" fill="{colors[index]}"/>')
        parts.append(f'<text x="{left + index*150}" y="{height-38}" font-family="sans-serif" font-size="12" fill="{colors[index]}">{names[index]}</text>')
    parts.append('</svg>')
    Path(filename).write_text('\n'.join(parts), encoding='utf-8')

plot('results/dp.svg', 'Dynamic programming', 'dp', 'random', ['LCS', 'Edit Distance'])
plot('results/matching-present.svg', 'String matching: present pattern', 'matching', 'present', ['Naive', 'KMP', 'Rabin-Karp'])
plot('results/matching-absent.svg', 'String matching: absent pattern', 'matching', 'absent', ['Naive', 'KMP', 'Rabin-Karp'])
