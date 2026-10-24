"""Reproduce the exact CTMC calculation and report data: .venv/bin/python solve.py."""
from pathlib import Path
import csv
import json
import numpy as np
import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt
from matplotlib.patches import FancyArrowPatch, Circle
import system1
import system2

ROOT = Path(__file__).resolve().parent
OUT = ROOT / 'results'


def csv_write(name, header, rows):
    with (OUT / name).open('w', encoding='utf-8-sig', newline='') as f:
        writer = csv.writer(f)
        writer.writerow(header)
        writer.writerows(rows)


def linear(weights):
    terms = [f'{float(w):.15g}*p{i}' for i, w in enumerate(weights) if w != 0]
    return '(' + '+'.join(terms or ['0']) + ')'


def export_mark(index, model):
    """Text for manual entry, not an undocumented native WinMARK project file."""
    q = model['Q']
    states = model['states']
    lines = [f'SYSTEM {index}; dimension={len(q)}; source=row, destination=column',
             'Enter off-diagonal nonzero values below; all others 0.',
             'WinMARK calculates diagonal values automatically.',
             'State numbering and probabilities start at 0.', '']
    for i, state in enumerate(states):
        lines.append(f'p{i}: {state}')
    lines += ['', 'OFF-DIAGONAL RATES / SECOND:']
    lines += [f'{i},{j}: {q[i,j]:.15g}' for i in range(len(q)) for j in range(len(q)) if i != j and q[i,j] != 0]
    if index == 1:
        busy = [np.array([0, 1, 0] + [1] * 8), np.array([0, 0, 1] + [1] * 8)]
        queue = np.array([0, 0, 0] + list(range(8)))
        accept = np.array([1] * 10 + [0])
        wait_reward = np.array([0, 0, 0] + [(k + 1) * 5 for k in range(7)] + [0])
        loss = 'p10'
    else:
        busy = [np.repeat(np.arange(5) > 0, 7), np.tile(np.arange(7) > 0, 5)]
        queue = np.repeat(np.maximum(np.arange(5) - 1, 0), 7) + np.tile([0, 0, 0, 0, 1, 1, 1], 5)
        loss = f'(0.65*{linear(np.repeat(np.arange(5) == 4,7))}+0.35*{linear(np.tile(np.arange(7) >= 4,5))})'
        accept = 0.65 * np.repeat(np.arange(5) < 4, 7) + 0.35 * np.tile(np.arange(7) < 4, 5)
        wait_reward = 0.65 * np.repeat([0, 10, 20, 30, 0], 7) + 0.35 * np.tile([0, 10, 20/3, 10/3, 0, 0, 0], 5)
    formulas = {
        'Busy1': linear(busy[0]), 'Busy2': linear(busy[1]),
        'Busy': 'Busy1+Busy2', 'Queue': linear(queue), 'Number': 'Busy+Queue',
        'Loss': loss, 'Throughput': '0.5*(1-Loss)',
        'Wait': f'{linear(wait_reward)}/{linear(accept)}',
        'Sojourn': 'Wait+10', 'Idle': 'p0',
    }
    lines += ['', 'CHARACTERISTIC FORMULAS (enter in this order):']
    lines += [f'{key} = {value}' for key, value in formulas.items()]
    (OUT / f'mark_system{index}.txt').write_text('\n'.join(lines) + '\n', encoding='utf-8')


def check_model(model):
    q, pi, m = model['Q'], model['pi'], model['metrics']
    offdiag = q.copy()
    np.fill_diagonal(offdiag, 0)
    errors = {
        'generator_row_sum': float(np.max(np.abs(q.sum(axis=1)))),
        'stationary_residual': float(np.max(np.abs(pi @ q))),
        'normalization': float(abs(pi.sum() - 1)),
        'flow_conservation': abs(m['total_throughput'] - .5 * (1 - m['total_loss'])),
        'service_time_identity': max(abs(m['throughput'][i] * 10 - m['busy'][i]) for i in range(2)),
        'little_queue_check_only': abs(m['total_queue'] - m['total_throughput'] * m['total_wait']),
        'little_number_check_only': abs(m['total_number'] - m['total_throughput'] * m['total_sojourn']),
    }
    if 'accepted_throughput' in m:
        errors['per_server_arrival_completion'] = max(
            abs(a - x) for a, x in zip(m['accepted_throughput'], m['throughput']))
    assert np.min(pi) >= -1e-13 and np.min(offdiag) >= 0
    assert all(x < 1e-10 for x in errors.values()), errors
    return errors


def draw_graph1():
    fig, ax = plt.subplots(figsize=(11, 3.6))
    positions = {'E': (0, 0), 'A': (1.6, 1), 'B': (1.6, -1)}
    positions.update({f'C{i}': (3.2 + 1.2*i, 0) for i in range(8)})
    def edge(a, b, label, rad=.15):
        x, y = positions[a], positions[b]
        ax.add_patch(FancyArrowPatch(x, y, arrowstyle='-|>', mutation_scale=10,
                                    connectionstyle=f'arc3,rad={rad}', shrinkA=15, shrinkB=15, lw=.9))
        mid = ((x[0]+y[0])/2, (x[1]+y[1])/2)
        dx, dy = y[0]-x[0], y[1]-x[1]
        ax.text(mid[0]+rad*dy, mid[1]-rad*dx, label, fontsize=8, ha='center', va='center',
                bbox={'facecolor':'white','edgecolor':'none','pad':.5})
    for a,b,up,down in [('E','A','λp','μ'),('E','B','λ(1−p)','μ'),('A','C0','λ','μ'),('B','C0','λ','μ')]:
        edge(a,b,up,.18)
        edge(b,a,down,.18)
    for i in range(7):
        edge(f'C{i}', f'C{i+1}', 'λ', .35)
        edge(f'C{i+1}', f'C{i}', '2μ', .35)
    for label, xy in positions.items():
        ax.add_patch(Circle(xy,.24,facecolor='white',edgecolor='black',zorder=5))
        ax.text(*xy,label,ha='center',va='center',fontsize=9,zorder=6)
    ax.set(xlim=(-.4,12.3),ylim=(-1.55,1.55),aspect='equal')
    ax.axis('off')
    fig.tight_layout()
    fig.savefig(OUT/'graph_system1.svg', bbox_inches='tight')
    plt.close(fig)


def draw_graph2():
    # Cartesian-product graph: every horizontal edge is copied in all seven rows;
    # every phase/arrival/service edge is copied in all five columns.
    fig, ax = plt.subplots(figsize=(10, 8))
    states2 = ['0','(1,1)','(1,2)','(1,3)','(2,1)','(2,2)','(2,3)']
    positions = {(n,j):(2.2*n, -1.3*j) for n in range(5) for j in range(7)}
    edges2 = [(0,1,'λ₂'), (1,2,'ν'), (2,3,'ν'), (3,0,'ν'),
              (1,4,'λ₂'), (2,5,'λ₂'), (3,6,'λ₂'),
              (4,5,'ν'), (5,6,'ν'), (6,1,'ν')]
    for j in range(7):
        for n in range(4):
            for a,b,label in [((n,j),(n+1,j),'λ₁'),((n+1,j),(n,j),'μ')]:
                x,y=positions[a],positions[b]
                ax.add_patch(FancyArrowPatch(x,y,arrowstyle='-|>',mutation_scale=6,connectionstyle='arc3,rad=.18',shrinkA=13,shrinkB=13,lw=.65,color='#555555'))
                ax.text((x[0]+y[0])/2,(x[1]+y[1])/2 + (-.25 if a[0]<b[0] else .25),label,fontsize=7,ha='center',va='center',bbox={'facecolor':'white','edgecolor':'none','pad':.2})
    for n in range(5):
        for ja,jb,label in edges2:
            x,y=positions[n,ja],positions[n,jb]
            curved = abs(ja-jb)>1
            rad = .38 if curved else 0
            ax.add_patch(FancyArrowPatch(x,y,arrowstyle='-|>',mutation_scale=6,connectionstyle=f'arc3,rad={rad}',shrinkA=13,shrinkB=13,lw=.6,color='#777777'))
            dx=(.27 if jb>ja else -.5) if curved else .12
            ax.text(x[0]+dx,(x[1]+y[1])/2,label,fontsize=7,ha='center',bbox={'facecolor':'white','edgecolor':'none','pad':.2})
    for (n,j),xy in positions.items():
        ax.add_patch(Circle(xy,.22,facecolor='white',edgecolor='black',lw=.75,zorder=5))
        ax.text(*xy,str(7*n+j),ha='center',va='center',fontsize=7,zorder=6)
    for n in range(5):
        ax.text(2.2*n,.65,f'n₁ = {n}',ha='center',fontsize=9)
    for j,label in enumerate(states2):
        ax.text(-.8,-1.3*j,label,ha='right',va='center',fontsize=9)
    ax.set(xlim=(-1.9,9.6),ylim=(-8.25,1),aspect='equal')
    ax.axis('off')
    fig.tight_layout()
    fig.savefig(OUT/'graph_system2.svg',bbox_inches='tight')
    plt.close(fig)


def main():
    OUT.mkdir(exist_ok=True)
    plt.rcParams.update({'font.family':'DejaVu Sans','font.size':10,'svg.fonttype':'none'})
    models = [system1.analyze(), system2.analyze()]
    diagnostics = {}
    data = {'parameters': {'lambda':.5,'b':10,'p1':.65,'p2':.35}, 'systems': []}
    for i,model in enumerate(models,1):
        diagnostics[f'system{i}'] = check_model(model)
        csv_write(f'states_system{i}.csv',['index','state','probability'],
                  [[j,state,float(model['pi'][j])] for j,state in enumerate(model['states'])])
        csv_write(f'Q_system{i}.csv',['source/destination']+list(range(len(model['Q']))),
                  [[j]+list(row) for j,row in enumerate(model['Q'])])
        data['systems'].append({'states':model['states'],'pi':model['pi'].tolist(),
                                'Q':model['Q'].tolist(),'metrics':model['metrics']})
        export_mark(i,model)
    # Independent population-count birth/death solution for system1.
    r=5.0
    e=1/(1+r+r*r/2*sum((r/2)**k for k in range(8)))
    expected=np.array([e, (r*.65+r*r/2)/(r+1)*e,
                       (r*.35+r*r/2)/(r+1)*e]+[r*r/2*e*(r/2)**k for k in range(8)])
    diagnostics['system1_closed_form'] = float(np.max(np.abs(expected-models[0]['pi'])))
    # Independently solve each factor by summing the product's generator rates.
    from common import stationary
    q=models[1]['Q']
    q1=np.array([[sum(q[7*a,7*b:7*b+7]) for b in range(5)] for a in range(5)])
    q2=np.array([[sum(q[a,b::7]) for b in range(7)] for a in range(7)])
    product=np.kron(stationary(q1),stationary(q2))
    diagnostics['system2_product_form']=float(np.max(np.abs(product-models[1]['pi'])))
    diagnostics['system2_full_linear_solve'] = float(np.max(np.abs(stationary(q)-models[1]['pi'])))
    assert diagnostics['system2_full_linear_solve'] < 1e-12
    assert diagnostics['system1_closed_form'] < 1e-12
    assert diagnostics['system2_product_form'] < 1e-12
    # Exercise the parameterized models, including very low and high positive loads.
    grid=np.linspace(.05,1,96)
    curves=[]
    for lam in grid:
        curves.append([system1.analyze(float(lam))['metrics'],system2.analyze(float(lam))['metrics']])
    plot_keys=[('total_loss','Вероятность потери'),('total_throughput','Производительность, 1/с'),
               ('total_busy','Среднее число занятых приборов'),('total_queue','Средняя суммарная очередь'),
               ('total_wait','Среднее ожидание, с'),('total_sojourn','Среднее пребывание, с')]
    csv_write('sensitivity.csv',['lambda']+[f'system{i}_{key}' for key,_ in plot_keys for i in (1,2)],
              [[float(lam)]+[row[i][key] for key,_ in plot_keys for i in (0,1)] for lam,row in zip(grid,curves)])
    for page in range(3):
        fig,axes=plt.subplots(2,1,figsize=(7.2,7.6),layout='constrained')
        for ax,(key,title) in zip(axes,plot_keys[2*page:2*page+2]):
            for i,color in [(0,'#205a8e'),(1,'#a24a30')]:
                ax.plot(grid,[row[i][key] for row in curves],label=f'СИСТЕМА_{i+1}',color=color,lw=1.8)
                ax.plot(.5,models[i]['metrics'][key],'o',color=color,ms=5)
            ax.axvline(.5,color='gray',ls=':',lw=.9)
            ax.set(xlabel='Интенсивность входного потока λ, 1/с',ylabel=title,xlim=(.05,1))
            ax.grid(alpha=.22)
            ax.legend(fontsize=9)
        fig.savefig(OUT/f'comparison{page+1}.svg')
        plt.close(fig)
    draw_graph1()
    draw_graph2()
    data['verification']=diagnostics
    (OUT/'data.json').write_text(json.dumps(data,ensure_ascii=False,indent=2),encoding='utf-8')
    csv_write('characteristics.csv',['metric','system1','system2'],
              [[key,models[0]['metrics'][key],models[1]['metrics'][key]] for key,_ in plot_keys])
    print(json.dumps({'metrics':[m['metrics'] for m in models],'verification':diagnostics},ensure_ascii=False,indent=2))


if __name__ == '__main__':
    main()
