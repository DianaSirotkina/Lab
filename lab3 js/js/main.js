function start() {
    var matrix = [
        ["Y/X", 7.2, 10, 12.8, "Pyi"],
        [0.8, 0.12, 0.04, 0.13, 0.29],          //создаем матрицу
        [1.6, 0.22, 0.12, 0.13, 0.47],
        [2.4, 0.06, 0.141, 0.039, 0.24],
        ["Pxj", 0.4, 0.301, 0.299, 1]
    ];

    console.table(matrix); //Выводи матрицу в виде таблицы в консоль

    var MX = 0,
        MX2 = 0,
        DX = 0,
        MY = 0,
        MY2= 0,             //Объявляем переменные для подсчета значений
        DY= 0,  
        MXY= 0,
        Kxy = 0,
        Rxy = 0;

    for (var i = 1; i <= 3; i++){ //Расчитываем значения
        MX = MX + (matrix[0][i] * matrix[4][i]);
        MX2 = MX2 + ((matrix[0][i]*matrix[0][i]) * matrix[4][i]);
        MY = MY + (matrix[i][0] * matrix[i][4]);
        MY2 = MY2 + ((matrix[i][0]*matrix[i][0]) * matrix[i][4]);
    }

    DX = MX2 - (MX*MX);
    DY = MY2 - MY*MY;
    var sigmaX = Math.sqrt(DX);
    var sigmaY = Math.sqrt(DY);

    for (i = 1; i <= 3; i++){
        for (var j = 1; j<=3; j++){
            MXY = MXY + (matrix[i][0] * matrix[0][j] * matrix[i][j]);
        }
    }

    Kxy = MXY - MX*MY;
    Rxy = Kxy/(sigmaX*sigmaY);

    //Объявляем массивы и переменные
    var arrayXY = []; //Массив для хранения пар значений X и Y
    var value = parseInt(document.getElementById("textvalue").value); //считываем кол-во случайных величин
    var statusY = 0;
    var statusX = 0;
    var statusXY = [0,0,0,0,0,0,0,0,0]; //здесь будет храниться число одинаковых пар


    //Геннерируем рандомные числа числам и вешаем на них статусы по X и Y
    for (i = 0; i < value; i++){
        var rand = Math.random(); //Генерируем рандомное число

        if (rand <= matrix[4][1]){
            statusX = 1;

        }

        else if (rand <= matrix[4][2] + matrix[4][1] && rand > matrix[4][2]){
            statusX = 2;
        }

        else {
            statusX = 3;
        }

        rand = Math.random(); //Генерируем рандомное число

        if (rand <= matrix[1][4]){
            arrayXY.push([[matrix[4][statusX]],[matrix[1][4]]]);
            statusY = 1;
        }

        else if(rand <= (matrix[2][4] + matrix[1][4])){
            arrayXY.push([[matrix[4][statusX]],[matrix[2][4]]]);
            statusY = 2;
        }

        else {
            arrayXY.push([[matrix[4][statusX]],[matrix[3][4]]]);
            statusY = 3;
        }
            
        //Проверяем статусы и записываем кол-во на соответствующую пару значений
        if (statusX == 1 && statusY == 1){statusXY[0] += 1;}
        else if (statusX == 1 && statusY == 2){statusXY[1] += 1;}
        else if (statusX == 1 && statusY == 3){statusXY[2] += 1;}

        if (statusX == 2 && statusY == 1){statusXY[3] += 1;}
        else if (statusX == 2 && statusY == 2){statusXY[4] += 1;}
        else if (statusX == 2 && statusY == 3){statusXY[5] += 1;}

        if (statusX == 3 && statusY == 1){statusXY[6] += 1;}
        else if (statusX == 3 && statusY == 2){statusXY[7] += 1;}
        else if (statusX == 3 && statusY == 3){statusXY[8] += 1;}
    }

    console.log(statusXY);

    var arrForX = [0,0,0]; //Массив для хранения кол-ва Х по диапазонам
    var arrForY = [0,0,0]; //Массив для хранения кол-ва Y по диапазонам
    console.log(arrayXY);
    for (i=0; i < value; i++){ //достаем X из массива пар значений X и Y 
        if (arrayXY[i][0] == matrix[4][1]){
            arrForX[0] += 1;
        }
        else if(arrayXY[i][0] == matrix[4][2]){
            arrForX[1] += 1;
        }
        else {
            arrForX[2] += 1;
        }

        if (arrayXY[i][1] == matrix[1][4]){ //достаем Y из массива пар значений X и Y
            arrForY[0] += 1;
        }
        else if(arrayXY[i][1] == matrix[2][4]){
            arrForY[1] += 1;
        }
        else {
            arrForY[2] += 1;
        }
    }

    console.log(arrForX);
    console.log(arrForY);


//Строим графики
    var incomeX = document.getElementById("incomeX").getContext("2d"); // Получение элемента на вебстранцие
    var barDataX = {
        labels : arrForX,
        datasets : [
            {
                fillColor : "#a020a4",
                strokeColor : "#d13fcc",
                data : arrForX
            }
        ]
    };
    new Chart(incomeX).Bar(barDataX);

    var incomeY = document.getElementById("incomeY").getContext("2d");
    var barDataY = {
        labels : arrForY,
        datasets : [
            {
                fillColor : "#a020a4",
                strokeColor : "#d13fcc",
                data : arrForY
            }

        ]
    };
    new Chart(incomeY).Bar(barDataY);

    var incomeXY = document.getElementById("incomeXY").getContext("2d");
    var barDataXY = {
        labels : [["x1","y1"],["x1","y2"],["x1","y3"],["x2","y1"],["x2","y2"],["x2","y3"], ["x3","y1"],["x3","y2"],["x3","y3"]],
        datasets : [
            {
                fillColor : "a020a4",
                strokeColor : "#d13fcc",
                data : statusXY
            }

        ]
    };
    new Chart(incomeXY).Bar(barDataXY);

    
    document.getElementById("MX").value = MX;
    document.getElementById("MX2").value = MX2;
    document.getElementById("DX").value = DX;
    document.getElementById("sigmaX").value = sigmaX;
    document.getElementById("MY").value = MY;
    document.getElementById("MY2").value = MY2;
    document.getElementById("DY").value = DY;
    document.getElementById("sigmaY").value = sigmaY;
    document.getElementById("MXY").value = MXY;
    document.getElementById("Kxy").value = Kxy;
    document.getElementById("Rxy").value = Rxy;
    console.log(matrix);
}
