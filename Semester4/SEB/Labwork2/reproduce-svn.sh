#!/bin/bash 

CMTS="/home/awesoma/ITMO_Labs/opi/lab2/commits"

svnadmin create repo
REPO_URL="file://$(pwd)/repo"

cd repo
svn mkdir -m "repo struct" $REPO_URL/trunk $REPO_URL/branches
cd ..

# wc
svn checkout $REPO_URL/trunk wc
cd wc

#r0
echo "#############r0############"
unzip -o $CMTS/commit0.zip -d .
svn add * --force
svn commit -m "r0" --username=red

#r1
echo "#############r1############"
svn copy $REPO_URL/trunk $REPO_URL/branches/b-r1 -m "br-2"
svn switch $REPO_URL/branches/b-r1

svn rm *
unzip -o $CMTS/commit1.zip -d .
svn add * --force
svn commit -m "r1" --username=blue

#r2
echo "#############r2############"
svn copy $REPO_URL/branches/b-r1 $REPO_URL/branches/b-r2 -m "b-r2"
svn switch $REPO_URL/branches/b-r2
unzip -o $CMTS/commit2.zip -d .
svn add * --force
svn commit -m "r2" --username=blue

#r3
echo "#############r3############"
svn copy $REPO_URL/branches/b-r2 $REPO_URL/branches/b-r3 -m "from r2 to b-r3"
svn switch $REPO_URL/branches/b-r3
unzip -o $CMTS/commit3.zip -d .
svn add * --force
svn commit -m "r3" --username=red

#r4
echo "#############r4############"
svn copy $REPO_URL/branches/b-r3 $REPO_URL/branches/b-r4 -m "from r3 to b-r4" --username=red
svn switch $REPO_URL/branches/b-r4 --username=red
unzip -o $CMTS/commit4.zip -d .
svn add * --force --username=red
svn commit -m "r4" --username=red

#r5
echo "#############r5############"
svn copy $REPO_URL/branches/b-r4 $REPO_URL/branches/b-r5 -m "from r4 to b-r5" --username=blue
svn switch $REPO_URL/branches/b-r5 --username=blue

unzip -o $CMTS/commit5.zip -d .
svn add * --force
svn commit -m "r5" --username=blue

svn update
svn merge $REPO_URL/branches/b-r4 

#r6
echo "#############r6############"
svn switch $REPO_URL/branches/b-r3 --username=red
unzip -o $CMTS/commit6.zip -d .
svn add * --force
svn commit -m "r6" --username=red

#r7
echo "#############r7############"
svn copy $REPO_URL/branches/b-r3 $REPO_URL/branches/b-r7 -m "from r6 to b-r7" --username=blue
svn switch $REPO_URL/branches/b-r7 --username=blue
unzip -o $CMTS/commit7.zip -d .
svn add * --force --username=blue
svn commit -m "r7" --username=blue

#r8
echo "#############r8############"
svn copy $REPO_URL/branches/b-r7 $REPO_URL/branches/b-r8 -m "from r7 to b-r8" --username=red
svn switch $REPO_URL/branches/b-r8 --username=red
unzip -o $CMTS/commit8.zip -d .
svn add * --force --username=red
svn commit -m "r8" --username=red

#r9
echo "#############r9############"
svn copy $REPO_URL/branches/b-r8 $REPO_URL/branches/b-r9 -m "from r8 to b-r9" --username=blue
svn switch $REPO_URL/branches/b-r9 --username=blue
unzip -o $CMTS/commit9.zip -d .
svn add * --force --username=blue
svn commit -m "r9" --username=blue

#r10
echo "#############r10############"
svn copy $REPO_URL/branches/b-r9 $REPO_URL/branches/b-r10 -m "from r9 to b-r10" --username=blue
svn switch $REPO_URL/branches/b-r10 --username=blue
unzip -o $CMTS/commit10.zip -d .
svn add * --force --username=blue
svn commit -m "r10" --username=blue

#r11
echo "#############r11############"
svn copy $REPO_URL/branches/b-r10 $REPO_URL/branches/b-r11 -m "from r10 to b-r11" --username=red
svn switch $REPO_URL/branches/b-r11 --username=red
unzip -o $CMTS/commit11.zip -d .
svn add * --force --username=red
svn commit -m "r11" --username=red

#r12
echo "#############r12############"
svn switch $REPO_URL/branches/b-r1 --username=blue
unzip -o $CMTS/commit12.zip -d .
svn add * --force --username=blue
svn commit -m "r12" --username=blue

#r13
echo "#############r13############"
svn copy $REPO_URL/branches/b-r1 $REPO_URL/branches/b-r13 -m "from r12 to b-r13" --username=blue
svn switch $REPO_URL/branches/b-r13 --username=blue
unzip -o $CMTS/commit13.zip -d .
svn add * --force --username=blue
svn commit -m "r13" --username=blue

#r14
echo "#############r14############"
svn copy $REPO_URL/branches/b-r13 $REPO_URL/branches/b-r14 -m "from r13 to b-r14" --username=blue
svn switch $REPO_URL/branches/b-r14 --username=blue
unzip -o $CMTS/commit14.zip -d .
svn add * --force --username=blue
svn commit -m "r14" --username=blue

#r15
echo "#############r15############"
svn copy $REPO_URL/branches/b-r14 $REPO_URL/branches/b-r15 -m "from r14 to b-r15" --username=red
svn switch $REPO_URL/branches/b-r15 --username=red
unzip -o $CMTS/commit15.zip -d .
svn add * --force --username=red
svn commit -m "r15" --username=red

#r16
echo "#############r16############"
svn copy $REPO_URL/branches/b-r15 $REPO_URL/branches/b-r16 -m "from r15 to b-r16" --username=blue
svn switch $REPO_URL/branches/b-r16 --username=blue

unzip -o $CMTS/commit16.zip -d .
svn add * --force
svn commit -m "r16" --username=blue

svn update
svn merge $REPO_URL/branches/b-r15 

#r17
echo "#############r17############"
svn switch $REPO_URL/branches/b-r2 --username=blue
unzip -o $CMTS/commit17.zip -d .
svn add * --force --username blue
svn commit -m "r17" --username=blue

#r18
echo "#############r18############"
svn copy $REPO_URL/branches/b-r2 $REPO_URL/branches/b-r18 -m "from r17 to b-r18" --username=red
svn switch $REPO_URL/branches/b-r18 --username=red
unzip -o $CMTS/commit18.zip -d .
svn add * --force --username=red
svn commit -m "r18" --username=red

#r19
echo "#############r19############"
svn copy $REPO_URL/branches/b-r18 $REPO_URL/branches/b-r19 -m "from r18 to b-r19" --username=blue
svn switch $REPO_URL/branches/b-r19 --username=blue
unzip -o $CMTS/commit19.zip -d .
svn add * --force --username=blue
svn commit -m "r19" --username=blue

#r20
echo "#############r20############"
svn copy $REPO_URL/branches/b-r19 $REPO_URL/branches/b-r20 -m "from r19 to b-r20" --username=red
svn switch $REPO_URL/branches/b-r20 --username=red
unzip -o $CMTS/commit20.zip -d .
svn add * --force --username=red
svn commit -m "r20" --username=red

#r21
echo "#############r21############"
svn switch $REPO_URL/branches/b-r16
unzip -o $CMTS/commit21.zip -d .
svn add * --force --username=blue
svn commit -m "r21" --username=blue

#r22
echo "#############r22############"
svn switch $REPO_URL/branches/b-r13
unzip -o $CMTS/commit22.zip -d .
svn add * --force --username=blue
svn commit -m "r22" --username=blue

#r23
echo "#############r23############"
svn switch $REPO_URL/branches/b-r5
unzip -o $CMTS/commit23.zip -d .
svn add * --force --username=blue
svn commit -m "r23" --username=blue

#r24
echo "#############r24############"
svn copy $REPO_URL/branches/b-r5 $REPO_URL/branches/b-r24 -m "from r23 to b-r24" --username=red
svn switch $REPO_URL/branches/b-r24 --username=red
unzip -o $CMTS/commit24.zip -d .
svn add * --force --username=red
svn commit -m "r24" --username=red

#r25
echo "#############r25############"
svn switch $REPO_URL/branches/b-r1
unzip -o $CMTS/commit25.zip -d .
svn add * --force --username=blue
svn commit -m "r25" --username=blue

#r26
echo "#############r26############"
svn copy $REPO_URL/branches/b-r1 $REPO_URL/branches/b-r26 -m "from r25 to b-r26" --username=blue
svn switch $REPO_URL/branches/b-r26 --username=blue
unzip -o $CMTS/commit26.zip -d .
svn add * --force --username=blue
svn commit -m "r26" --username=blue

#r27
echo "#############r27############"
svn switch $REPO_URL/branches/b-r16
unzip -o $CMTS/commit27.zip -d .
svn add * --force --username=blue
svn commit -m "r27" --username=blue

#r28
echo "#############r28############"
svn copy $REPO_URL/branches/b-r16 $REPO_URL/branches/b-r28 -m "from r27 to b-r28" --username=red
svn switch $REPO_URL/branches/b-r28 --username=red
unzip -o $CMTS/commit28.zip -d .
svn add * --force --username=red
svn commit -m "r28" --username=red

svn update 
svn merge $REPO_URL/branches/b-r16

#r29
echo "#############r29############"
svn copy $REPO_URL/branches/b-r28 $REPO_URL/branches/b-r29 -m "from r28 to b-r29" --username=red
svn switch $REPO_URL/branches/b-r29 --username=red
unzip -o $CMTS/commit29.zip -d .
svn add * --force --username=red
svn commit -m "r29" --username=red

#r30
echo "#############r30############"
svn copy $REPO_URL/branches/b-r29 $REPO_URL/branches/b-r30 -m "from r29 to b-r30" --username=red
svn switch $REPO_URL/branches/b-r30 --username=red
unzip -o $CMTS/commit30.zip -d .
svn add * --force --username=red
svn commit -m "r30" --username=red

#r31
echo "#############r31############"
svn switch $REPO_URL/branches/b-r5 --username=blue
unzip -o $CMTS/commit31.zip -d .
svn add * --force --username=blue
svn commit -m "r31" --username=blue

#r32
echo "#############r32############"
svn switch $REPO_URL/branches/b-r30 --username=red
unzip -o $CMTS/commit32.zip -d .
svn add * --force --username=red
svn commit -m "r32" --username=red

#r33
echo "#############r33############"
svn copy $REPO_URL/branches/b-r30 $REPO_URL/branches/b-r33 -m "from r32 to b-r33" --username=red
svn switch $REPO_URL/branches/b-r33 --username=red
unzip -o $CMTS/commit33.zip -d .
svn add * --force --username=red
svn commit -m "r33" --username=red

#r34
echo "#############r34############"
svn switch $REPO_URL/branches/b-r8 --username=red
unzip -o $CMTS/commit34.zip -d .
svn add * --force --username=red
svn commit -m "r34" --username=red

#r35
echo "#############r35############"
svn copy $REPO_URL/branches/b-r8 $REPO_URL/branches/b-r35 -m "r34-r35" --username=red
svn switch $REPO_URL/branches/b-r35 
#svn update
svn merge $REPO_URL/branches/b-r33 --username=red 

#svn resolve G.java
#svn resolve I.java

unzip -o $CMTS/commit35.zip -d .
svn add * --force --username=red
svn commit -m "r35" --username=red

#r36
echo "#############r36############"
svn switch $REPO_URL/branches/b-r9 --username=blue
unzip -o $CMTS/commit36.zip -d .
svn add * --force --username=blue
svn commit -m "r36" --username=blue

#r37
echo "#############r37############"
svn switch $REPO_URL/branches/b-r2 --username=blue
svn merge $REPO_URL/branches/b-r9 --username=blue 

#unzip -o $CMTS/commit37.zip -d .
#svn add * --force --username=blue
svn commit -m "r37" --username=blue

#r38
echo "#############r38############"
svn switch $REPO_URL/branches/b-r13 --username=blue
unzip -o $CMTS/commit38.zip -d .
svn add * --force --username=blue
svn commit -m "r38" --username=blue

#r39
echo "#############r39############"
svn switch $REPO_URL/branches/b-r35 --username=red
unzip -o $CMTS/commit39.zip -d .
svn add * --force --username=red
svn commit -m "r39" --username=red

#r40
echo "#############r40############"
svn switch $REPO_URL/trunk --username=red
svn merge $REPO_URL/branches/b-r35 --username=red 

unzip -o $CMTS/commit40.zip -d .
svn add * --force --username=red
svn commit -m "r40" --username=red

#r41
echo "#############r41############"
svn switch $REPO_URL/branches/b-r30 --username=red
unzip -o $CMTS/commit41.zip -d .
svn add * --force --username=red
svn commit -m "r41" --username=red

#r42
echo "#############r42############"
svn switch $REPO_URL/branches/b-r26 --username=blue
unzip -o $CMTS/commit42.zip -d .
svn add * --force --username=blue
svn commit -m "r42" --username=blue

#r43
echo "#############r43############"
svn switch $REPO_URL/branches/b-r3 --username=blue
svn merge $REPO_URL/branches/b-r26 --username=blue 

#unzip -o $CMTS/commit41.zip -d .
#svn add * --force --username=red
svn commit -m "r43" --username=red

#r44
echo "#############r44############"
svn switch $REPO_URL/branches/b-r30 --username=red
unzip -o $CMTS/commit44.zip -d .
svn add * --force --username=red
svn commit -m "r44" --username=red

#r45
echo "#############r45############"
svn switch $REPO_URL/branches/b-r19 --username=blue
svn merge $REPO_URL/branches/b-r30
  #conflicts
svn commit -m "r45" --username=red

#r46
echo "#############r46############"
svn switch $REPO_URL/branches/b-r11 --username=red
svn merge $REPO_URL/branches/b-r19
svn commit -m "r46" --username=red

#r47
echo "#############r47############"
unzip -o $CMTS/commit47.zip -d .
svn add * --force --username=red
svn commit -m "r47" --username=red

#r48
echo "#############r48############"
svn switch $REPO_URL/branches/b-r18 --username=red
unzip -o $CMTS/commit48.zip -d .
svn add * --force --username=red
svn commit -m "r48" --username=red

#r49
echo "#############r49############"
unzip -o $CMTS/commit49.zip -d .
svn add * --force --username=red
svn commit -m "r49" --username=red

#r50
echo "#############r50############"
svn switch $REPO_URL/branches/b-r28 --username=red
unzip -o $CMTS/commit50.zip -d .
svn add * --force --username=red
svn commit -m "r50" --username=red

#r51
echo "#############r51############"
svn switch $REPO_URL/branches/b-r18 --username=red
svn merge $REPO_URL/branches/b-r28 --username=red 
svn commit -m "r51" --username=red

#r52
echo "#############r52############"
#double bin
svn switch $REPO_URL/branches/b-r3 --username=red
unzip -o $CMTS/commit52.zip -d .
svn add * --force --username=red
svn commit -m "r52" --username=red

#r53
echo "#############r53############"
svn switch $REPO_URL/branches/b-r14 --username=blue
unzip -o $CMTS/commit53.zip -d .
svn add * --force --username=blue
svn commit -m "r53" --username=blue

#r54
echo "#############r54############"
svn switch $REPO_URL/branches/b-r24 --username=red
unzip -o $CMTS/commit54.zip -d .
svn add * --force --username=red
svn commit -m "r54" --username=red

#r55
echo "#############r55############"
svn copy $REPO_URL/branches/b-r24 $REPO_URL/branches/b-r55 -m "r54-55" --username=blue
unzip -o $CMTS/commit55.zip -d .
svn add * --force --username=blue
svn commit -m "r55" --username=blue

#r56
echo "#############r56############"
svn switch $REPO_URL/branches/b-r1 --username=blue
unzip -o $CMTS/commit56.zip -d .
svn add * --force --username=blue
svn commit -m "r56" --username=blue

#r57
echo "#############r57############"
unzip -o $CMTS/commit57.zip -d .
svn add * --force --username=blue
svn commit -m "r57" --username=blue

#r58
echo "#############r58############"
svn switch $REPO_URL/branches/b-r29 --username=red
unzip -o $CMTS/commit58.zip -d .
svn add * --force --username=red
svn commit -m "r58" --username=red

#r59
echo "#############r59############"
svn switch $REPO_URL/branches/b-r1 --username=blue
svn merge $REPO_URL/branches/b-r29 --username=blue 
#conflicts
svn commit -m "r59" --username=blue

#r60
echo "#############r60############"
svn switch $REPO_URL/branches/b-r5 --username=blue
svn merge $REPO_URL/branches/b-r1 --username=blue 
#conflicts
svn commit -m "r60" --username=blue

#r61
echo "#############r61############"
svn switch $REPO_URL/branches/b-r2 --username=blue
unzip -o $CMTS/commit61.zip -d .
svn add * --force --username=blue
svn commit -m "r61" --username=blue

#r62
echo "#############r62############"
svn copy $REPO_URL/branches/b-r2 $REPO_URL/branches/b-r62 -m "r61-62" --username=blue
unzip -o $CMTS/commit62.zip -d .
svn add * --force --username=blue
svn commit -m "r62" --username=blue

#r63
echo "#############r63############"
svn switch $REPO_URL/branches/b-r11 --username=red
unzip -o $CMTS/commit63.zip -d .
svn add * --force --username=red
svn commit -m "r63" --username=red

#r64
echo "#############r64############"
svn switch $REPO_URL/branches/b-r7 --username=blue
unzip -o $CMTS/commit64.zip -d .
svn add * --force --username=blue
svn commit -m "r64" --username=blue

#r65
echo "#############r65############"
svn switch $REPO_URL/branches/b-r3 --username=red
unzip -o $CMTS/commit65.zip -d .
svn add * --force --username=red
svn commit -m "r65" --username=red

#r66
echo "#############r66############"
svn switch $REPO_URL/branches/b-r62 --username=blue
unzip -o $CMTS/commit66.zip -d .
svn add * --force --username=blue
svn commit -m "r66" --username=blue

#r67
echo "#############r67############"
svn switch $REPO_URL/branches/b-r11 --username=red
svn merge $REPO_URL/branches/b-r62 --username=red 
    #conflicts
svn commit -m "r67" --username=red

#r68
echo "#############r68############"
svn switch $REPO_URL/branches/b-r7 --username=blue
unzip -o $CMTS/commit68.zip -d .
svn add * --force --username=blue
svn commit -m "r68" --username=blue

#r69
echo "#############r69############"
unzip -o $CMTS/commit69.zip -d .
svn add * --force --username=blue
svn commit -m "r69" --username=blue

#r70
echo "#############r70############"
svn switch $REPO_URL/branches/b-r18 --username=red
unzip -o $CMTS/commit70.zip -d .
svn add * --force --username=red
svn commit -m "r70" --username=red

#r71
echo "#############r71############"
svn switch $REPO_URL/branches/b-r3 --username=red
svn merge $REPO_URL/branches/b-r18 --username=red 
    #conflicts
svn revert eqkVEXsynE.PhR
svn update eqkVEXsynE.PhR
svn resolve --accept=working eqkVEXsynE.PhR

svn commit -m "r71" --username=red

#r72
echo "#############r72############"
unzip -o $CMTS/commit72.zip -d .
svn add * --force --username=red
svn commit -m "r72" --username=red

#r73
echo "#############r73############"
svn switch $REPO_URL/branches/b-r10 --username=blue
unzip -o $CMTS/commit73.zip -d .
svn add * --force --username=blue
svn commit -m "r73" --username=blue

#r74
echo "#############r74############"
svn switch $REPO_URL/branches/b-r18 --username=red
unzip -o $CMTS/commit74.zip -d .
svn add * --force --username=red
svn commit -m "r74" --username=red

#75
svn switch $REPO_URL/branches/b-r55 --username=blue
svn merge $REPO_URL/branches/b-r18 --username=blue 
    #conflicts double bin
svn commit -m "r75" --username=blue

#r76
echo "#############r76############"
svn switch $REPO_URL/branches/b-r2 --username=blue
svn merge $REPO_URL/branches/b-r55 --username=blue 
    #conflicts
svn commit -m "r76" --username=blue

#r77
echo "#############r77############"
svn switch $REPO_URL/branches/b-r11 --username=red
svn merge $REPO_URL/branches/b-r2 --username=red 
    #conflicts
svn commit -m "r77" --username=red

#r78
echo "#############r78############"
svn switch $REPO_URL/branches/b-r10 --username=blue
svn merge $REPO_URL/branches/b-r77 --username=blue 
svn commit -m "r78" --username=blue

#r79
echo "#############r79############"
svn switch $REPO_URL/branches/b-r13 --username=blue
svn merge $REPO_URL/branches/b-r10 --username=blue 
    #conflicts
svn commit -m "r79" --username=blue

#r80
echo "#############r80############"
svn switch $REPO_URL/branches/b-r7 --username=blue
svn merge $REPO_URL/branches/b-r13 --username=blue 
    #conflicts
svn commit -m "r80" --username=blue

#r81
echo "#############r81############"
svn switch $REPO_URL/branches/b-r5 --username=blue
svn merge $REPO_URL/branches/b-r7 --username=blue 
    #conflicts
svn commit -m "r81" --username=blue

#r82
echo "#############r82############"
svn switch $REPO_URL/branches/b-r24 --username=red
svn merge $REPO_URL/branches/b-r5 --username=red 
svn commit -m "r82" --username=red

#r83
echo "#############r83############"
svn switch $REPO_URL/branches/b-r20 --username=red
svn merge $REPO_URL/branches/b-r24 --username=red 
svn commit -m "r83" --username=red

#84
echo "#############r84############"
svn switch $REPO_URL/branches/b-r3 --username=red
svn merge $REPO_URL/branches/b-r20 --username=red 
    #conflicts !!
svn commit -m "r84" --username=red

#85
echo "#############r85############"
svn switch $REPO_URL/trunk --username=red
svn merge $REPO_URL/branches/b-r3 --username=red 
    #conflicts !!
svn commit -m "r85" --username=red
