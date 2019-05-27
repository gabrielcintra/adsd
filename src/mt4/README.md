![UFCG](https://seeklogo.com/images/U/ufcg_universidade_federal_de_campina_grande-logo-E8B3971276-seeklogo.com.png "UFCG")
# Mini-teste 4 (Validação)
ADSD - UFCG 2019.1

##### Utilizado:
- [Arena Simulator](https://www.arenasimulation.com/) (Arena Simulator)

##### Metodologia de resolução:
Distribuição de valores no modelo, cálculo de métricas e replicação das métricas no simulador para análise.

---

##### Modelo seguido
![Modelo](https://i.ibb.co/NCJq365/Modelo.png "Modelo")

##### Tabela referência
![Tabela](https://i.ibb.co/8cLxVmq/Tabela.png "Tabela")

---

##### Análise de saída
Pode ser verificada no arquivo docs/report.pdf e comparado com o arquivo dos cálculos presentes em docs/tabela.png

A execução no Arena Simulator foi realizada na casa de minutos. Portanto, deve haver a divisão por 60 (Minutos) em comparações.

Por exemplo, comparando a taxa de chegada dos elementos, observamos:

> Taxa de chegada (Arena | Tabela)
C1: 1491/60 = 24.85 | Tabela: 24.00
C2: 1200/60 = 20.00 | Tabela: 16.00
C3: 714.80/60 = 11.91 | Tabela: 9.6
C4: 484/60 = 8.06 | Tabela: 6.40

Uma discrepância pode ser observada, mas ainda numa faixa de diferença aceitável.

> Fator utilização (Arena | Tabela)
C1: 0.71 | Tabela: 0.69
C2: 0.53 | Tabela: 0.50
C3: 0.47 | Tabela: 0.38
C4: 0.80 | Tabela: 0.64

Nota-se uma discrepância nos valores que aumenta à medida que os elementos vão se aprofundando neste fluxo. O mesmo se observa nas análises posteriores: ao se aplicar as fórmulas do número médio de fregueses e tempo médio, observadas também via report gerado pelo Arena, há discrepância principalmente nos elementos C3 e C4 devido o funcionamento do Arena e a dependência da execução utilizada na comparação.