TITLE "temperature distribution"
  SELECT
    errlim = 1e-5
  VARIABLES
    Temp
  DEFINITIONS
    hl = 0.4                            {толщина вертикальной части (штампа)}
    hw = 1.5                            {половина ширины горизонтальной планки}
    K = 1                               {коэффициент теплопроводности}
    Source = 100*exp(-x^2-(y-1.41)^2)   {функция источника (может быть опущена)}
    hf = 1                              {коэффициент теплообмена}
    Text = 0                            {температура окружающей среды}
    flux = -K*grad(Temp)                {вектор потока тепла}
  EQUATIONS
    div(flux) = Source                  {уравнение теплопроводности}
  BOUNDARIES
    REGION 1
      START (-hw,2.5)
      NATURAL(Temp) = -hf*(Text-Temp)   {верхняя горизонтальная линия с конвективным теплообменом}
      LINE TO (hw,2.5)
      NATURAL(Temp) = 0
      LINE TO (hw, 2.5 - hl)
      LINE TO (hl/2, 2.5 - hl)
      LINE TO (hl/2, -1)
      VALUE(Temp) = 10                  {нижняя граница вертикальной части с заданной температурой}
      LINE TO (-hl/2, -1)
      NATURAL(Temp) = 0
      LINE TO (-hl/2, 2.5 - hl)
      LINE TO (-hw, 2.5 - hl)
      NATURAL(Temp) = 0
      LINE TO CLOSE
  MONITORS
    CONTOUR(Temp)
  PLOTS
    GRID(x,y) as "Elements grid"
    CONTOUR(Temp) painted as "Temperature field"
    VECTOR(flux) as "Heat flow"
    CONTOUR(Source) painted as "Source"
END
