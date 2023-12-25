#!/usr/bin/bash

# Задание 1
step1() {
    echo "--Step 1"
    
    mkdir lab0
    cd lab0
    
    mkdir -p pichu0/garbodor pichu0/metapod pichu0/loudred shellder6/ferrothorn shellder6/mareep shellder6/finneon shellder6/gorebyss shellder6/cascoon shellder6/larvitar togekiss8/braviary togekiss8/servine
    
    cat > gulpin4 << EOF
    Возможности Overland=4 Surface=2 Jump=2 Power=2
    Intelligence=3 Amorphous=0 Inflatable=0
EOF
    
    cat > pichu0/hitmontop << EOF
    Способности Focus
    Energy Pursuit Quick Attack Triple Kick Rapid Spin Counter Feint
    Agility Gyro Ball Quick Guard Wide Guard Detect Close Combat
    Endeavor
EOF
    
    cat > roggenrola9 << EOF
    Способности Tackle Harden Sand-Attack Headbutt
    Rock Blast Mud-Slap Iron Defense Smack Down Power Gem Rock Slide
    Stealth Rock Sandstorm Stone Edge Explosion
EOF
    
    cat > togekiss8/togepi << EOF
    Тип диеты
    Herbivore
EOF
    
    cat > wailmer2 << EOF
    Возможности Overland=4 Surface=6 Underwater=6
    Jump=3 Power=4 Intelligence=3 Fountain=0
EOF
}

# Задание 2
step2() {
    echo "--Step 2"
    
    chmod 620 gulpin4
    chmod 576 pichu0
    chmod 764 pichu0/garbodor
    chmod ug-rwx,o=r pichu0/hitmontop
    chmod ug=wx,o-rwx pichu0/metapod
    chmod u=wx,g=rw,o=wx pichu0/loudred
    chmod u=rw,g=u-w roggenrola9
    chmod 333 shellder6
    chmod u=wx,g=u+r,o=rx shellder6/ferrothorn
    chmod 373 shellder6/mareep
    chmod u=rx,g=wx,o=rwx shellder6/finneon
    chmod 777 shellder6/gorebyss
    chmod 753 shellder6/cascoon
    chmod ugo=rx shellder6/larvitar
    chmod 511 togekiss8
    chmod 764 togekiss8/braviary
    chmod =rx togekiss8/servine
    chmod u=rw,g=w,o=w togekiss8/togepi
    chmod 604 wailmer2
}

# Задание 3
step3() {
    echo "--Step 3"
    
    #Владелец не может записывать в finneon, меняем права
    chmod u+w shellder6/finneon
    cp gulpin4 shellder6/finneon
    chmod u+w shellder6/finneon
    
    cat togekiss8/togepi togekiss8/togepi > gulpin4_64
    
    #togekiss8 не имеет прав на запись, меняем
    chmod u+w togekiss8/
    ln -s ../wailmer2 togekiss8/togepiwailmer
    chmod u-w togekiss8/
    
    cp -r togekiss8 shellder6/gorebyss
    
    #pichu0 r-x для владельца, меняем
    chmod u+w pichu0
    cp wailmer2 pichu0/hitmontopwailmer
    chmod u-w pichu0
    
    ln -s pichu0/ Copy__83
    
    #необходимо право записывать в pichu0
    chmod u+w pichu0
    ln roggenrola9 pichu0/hitmontoproggenrola
    chmod u-w pichu0
}

# Задание 4
step4() {
    echo "--Step 4"
    
    #Чтобы рекурсивно использовать wc
    shopt -s globstar
    
    wc -l **/g* | sort -r
    
    ls shellder6/ 2>&1 | sort -r
    
    cat -n pichu0/hitmontop togekiss8/togepi | grep -vi 't$'
    
    ls -ltrR **/*d 2>&1 | tail -n 2
    
    cat -n **/*p 2>/dev/null | sort -k 2
    
    cat -n togekiss8/* 2>&1 | grep -v 've'
}

# Задание 5
step5() {
    echo "--Step 5"
    
    rm gulpin4
    
    #Меняем права, чтобы появилась возможность удаления
    chmod u+w pichu0
    rm -f pichu0/hitmontop
    chmod u-w pichu0
    
    chmod u+w togekiss8
    rm togekiss8/togepiwailm*
    chmod u-w togekiss8
    
    chmod u+w pichu0/
    rm pichu0/hitmontoproggenro*
    chmod u-w pichu0/
    
    #Меняем права рекурсивно для pichu0
    chmod -R u+rwx pichu0/
    rm -r pichu0/
    
    #Уже удалена
    rmdir pichu0/metapod
}

step1
step2
step3
step4
step5
