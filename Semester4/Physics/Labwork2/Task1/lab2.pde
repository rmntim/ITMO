TITLE 'Task 1'
COORDINATES cartesian2
VARIABLES
  T
DEFINITIONS
  month=11
  day=30
EQUATIONS
  div(-grad(T))=0
BOUNDARIES
  REGION 1
    START(0,0)
    VALUE(T)=x          LINE TO (day,0)
    VALUE(T)=day        LINE TO (day,month)
    VALUE(T)=x^2/day    LINE TO (0,month)
    VALUE(T)=0          LINE TO CLOSE
PLOTS
  CONTOUR(T)
END
