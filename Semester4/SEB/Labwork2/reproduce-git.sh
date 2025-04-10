#!/bin/bash
set -e

# Path to the directory containing commit*.zip files
CMTS="$(realpath commits)"

# Create a new Git repository (using “main” as the trunk)
mkdir repo && cd repo
git init -b main

##########################
# r0 – initial commit on main (trunk)
##########################
echo "############# r0 #############"
# Unpack commit0 and commit into main with author red
rm -rf * 
unzip -o "$CMTS/commit0.zip" -d .
git add .
git commit -m "r0" --author="red <red@example.com>"

##########################
# r1 – create branch b-r1 from main, remove all files and commit commit1 with blue
##########################
echo "############# r1 #############"
git checkout -b b-r1 main
# Remove every file from the working directory (simulate svn rm *)
git rm -r --ignore-unmatch .
rm -rf *
unzip -o "$CMTS/commit1.zip" -d .
git add .
git commit -m "r1" --author="blue <blue@example.com>"

##########################
# r2 – branch b-r2 from b-r1 and commit commit2 with blue
##########################
echo "############# r2 #############"
git checkout -b b-r2 b-r1
unzip -o "$CMTS/commit2.zip" -d .
git add .
git commit -m "r2" --author="blue <blue@example.com>"

##########################
# r3 – branch b-r3 from b-r2 and commit commit3 with red
##########################
echo "############# r3 #############"
git checkout -b b-r3 b-r2
unzip -o "$CMTS/commit3.zip" -d .
git add .
git commit -m "r3" --author="red <red@example.com>"

##########################
# r4 – branch b-r4 from b-r3 and commit commit4 with red
##########################
echo "############# r4 #############"
git checkout -b b-r4 b-r3
unzip -o "$CMTS/commit4.zip" -d .
git add .
git commit -m "r4" --author="red <red@example.com>"

##########################
# r5 – branch b-r5 from b-r4 and commit commit5 with blue,
#      then merge b-r4 into b-r5
##########################
echo "############# r5 #############"
git checkout -b b-r5 b-r4
unzip -o "$CMTS/commit5.zip" -d .
git add .
git commit -m "r5" --author="blue <blue@example.com>"
# Merge b-r4 into b-r5
git merge b-r4 --no-ff -m "Merge b-r4 into b-r5"

##########################
# r6 – switch back to b-r3 and commit commit6 with red
##########################
echo "############# r6 #############"
git checkout b-r3
unzip -o "$CMTS/commit6.zip" -d .
git add .
git commit -m "r6" --author="red <red@example.com>"

##########################
# r7 – branch b-r7 from b-r3 and commit commit7 with blue
##########################
echo "############# r7 #############"
git checkout -b b-r7 b-r3
unzip -o "$CMTS/commit7.zip" -d .
git add .
git commit -m "r7" --author="blue <blue@example.com>"

##########################
# r8 – branch b-r8 from b-r7 and commit commit8 with red
##########################
echo "############# r8 #############"
git checkout -b b-r8 b-r7
unzip -o "$CMTS/commit8.zip" -d .
git add .
git commit -m "r8" --author="red <red@example.com>"

##########################
# r9 – branch b-r9 from b-r8 and commit commit9 with blue
##########################
echo "############# r9 #############"
git checkout -b b-r9 b-r8
unzip -o "$CMTS/commit9.zip" -d .
git add .
git commit -m "r9" --author="blue <blue@example.com>"

##########################
# r10 – branch b-r10 from b-r9 and commit commit10 with blue
##########################
echo "############# r10 #############"
git checkout -b b-r10 b-r9
unzip -o "$CMTS/commit10.zip" -d .
git add .
git commit -m "r10" --author="blue <blue@example.com>"

##########################
# r11 – branch b-r11 from b-r10 and commit commit11 with red
##########################
echo "############# r11 #############"
git checkout -b b-r11 b-r10
unzip -o "$CMTS/commit11.zip" -d .
git add .
git commit -m "r11" --author="red <red@example.com>"

##########################
# r12 – switch to b-r1 and commit commit12 with blue
##########################
echo "############# r12 #############"
git checkout b-r1
unzip -o "$CMTS/commit12.zip" -d .
git add .
git commit -m "r12" --author="blue <blue@example.com>"

##########################
# r13 – branch b-r13 from b-r1 and commit commit13 with blue
##########################
echo "############# r13 #############"
git checkout -b b-r13 b-r1
unzip -o "$CMTS/commit13.zip" -d .
git add .
git commit -m "r13" --author="blue <blue@example.com>"

##########################
# r14 – branch b-r14 from b-r13 and commit commit14 with blue
##########################
echo "############# r14 #############"
git checkout -b b-r14 b-r13
unzip -o "$CMTS/commit14.zip" -d .
git add .
git commit -m "r14" --author="blue <blue@example.com>"

##########################
# r15 – branch b-r15 from b-r14 and commit commit15 with red
##########################
echo "############# r15 #############"
git checkout -b b-r15 b-r14
unzip -o "$CMTS/commit15.zip" -d .
git add .
git commit -m "r15" --author="red <red@example.com>"

##########################
# r16 – branch b-r16 from b-r15 and commit commit16 with blue,
#      then merge b-r15 into b-r16
##########################
echo "############# r16 #############"
git checkout -b b-r16 b-r15
unzip -o "$CMTS/commit16.zip" -d .
git add .
git commit -m "r16" --author="blue <blue@example.com>"
git merge b-r15 --no-ff -m "Merge b-r15 into b-r16"

##########################
# r17 – switch to b-r2 and commit commit17 with blue
##########################
echo "############# r17 #############"
git checkout b-r2
unzip -o "$CMTS/commit17.zip" -d .
git add .
git commit -m "r17" --author="blue <blue@example.com>"

##########################
# r18 – branch b-r18 from b-r2 and commit commit18 with red
##########################
echo "############# r18 #############"
git checkout -b b-r18 b-r2
unzip -o "$CMTS/commit18.zip" -d .
git add .
git commit -m "r18" --author="red <red@example.com>"

##########################
# r19 – branch b-r19 from b-r18 and commit commit19 with blue
##########################
echo "############# r19 #############"
git checkout -b b-r19 b-r18
unzip -o "$CMTS/commit19.zip" -d .
git add .
git commit -m "r19" --author="blue <blue@example.com>"

##########################
# r20 – branch b-r20 from b-r19 and commit commit20 with red
##########################
echo "############# r20 #############"
git checkout -b b-r20 b-r19
unzip -o "$CMTS/commit20.zip" -d .
git add .
git commit -m "r20" --author="red <red@example.com>"

##########################
# r21 – switch to b-r16 and commit commit21 with blue
##########################
echo "############# r21 #############"
git checkout b-r16
unzip -o "$CMTS/commit21.zip" -d .
git add .
git commit -m "r21" --author="blue <blue@example.com>"

##########################
# r22 – switch to b-r13 and commit commit22 with blue
##########################
echo "############# r22 #############"
git checkout b-r13
unzip -o "$CMTS/commit22.zip" -d .
git add .
git commit -m "r22" --author="blue <blue@example.com>"

##########################
# r23 – switch to b-r5 and commit commit23 with blue
##########################
echo "############# r23 #############"
git checkout b-r5
unzip -o "$CMTS/commit23.zip" -d .
git add .
git commit -m "r23" --author="blue <blue@example.com>"

##########################
# r24 – branch b-r24 from b-r5 and commit commit24 with red
##########################
echo "############# r24 #############"
git checkout -b b-r24 b-r5
unzip -o "$CMTS/commit24.zip" -d .
git add .
git commit -m "r24" --author="red <red@example.com>"

##########################
# r25 – switch to b-r1 and commit commit25 with blue
##########################
echo "############# r25 #############"
git checkout b-r1
unzip -o "$CMTS/commit25.zip" -d .
git add .
git commit -m "r25" --author="blue <blue@example.com>"

##########################
# r26 – branch b-r26 from b-r1 and commit commit26 with blue
##########################
echo "############# r26 #############"
git checkout -b b-r26 b-r1
unzip -o "$CMTS/commit26.zip" -d .
git add .
git commit -m "r26" --author="blue <blue@example.com>"

##########################
# r27 – switch to b-r16 and commit commit27 with blue
##########################
echo "############# r27 #############"
git checkout b-r16
unzip -o "$CMTS/commit27.zip" -d .
git add .
git commit -m "r27" --author="blue <blue@example.com>"

##########################
# r28 – branch b-r28 from b-r16 and commit commit28 with red,
#      then merge b-r16 into b-r28
##########################
echo "############# r28 #############"
git checkout -b b-r28 b-r16
unzip -o "$CMTS/commit28.zip" -d .
git add .
git commit -m "r28" --author="red <red@example.com>"
git merge b-r16 --no-ff -m "Merge b-r16 into b-r28"

##########################
# r29 – branch b-r29 from b-r28 and commit commit29 with red
##########################
echo "############# r29 #############"
git checkout -b b-r29 b-r28
unzip -o "$CMTS/commit29.zip" -d .
git add .
git commit -m "r29" --author="red <red@example.com>"

##########################
# r30 – branch b-r30 from b-r29 and commit commit30 with red
##########################
echo "############# r30 #############"
git checkout -b b-r30 b-r29
unzip -o "$CMTS/commit30.zip" -d .
git add .
git commit -m "r30" --author="red <red@example.com>"

##########################
# r31 – switch to b-r5 and commit commit31 with blue
##########################
echo "############# r31 #############"
git checkout b-r5
unzip -o "$CMTS/commit31.zip" -d .
git add .
git commit -m "r31" --author="blue <blue@example.com>"

##########################
# r32 – switch to b-r30 and commit commit32 with red
##########################
echo "############# r32 #############"
git checkout b-r30
unzip -o "$CMTS/commit32.zip" -d .
git add .
git commit -m "r32" --author="red <red@example.com>"

##########################
# r33 – branch b-r33 from b-r30 and commit commit33 with red
##########################
echo "############# r33 #############"
git checkout -b b-r33 b-r30
unzip -o "$CMTS/commit33.zip" -d .
git add .
git commit -m "r33" --author="red <red@example.com>"

##########################
# r34 – switch to b-r8 and commit commit34 with red
##########################
echo "############# r34 #############"
git checkout b-r8
unzip -o "$CMTS/commit34.zip" -d .
git add .
git commit -m "r34" --author="red <red@example.com>"

##########################
# r35 – branch b-r35 from b-r8, merge in b-r33, then commit commit35 with red
##########################
echo "############# r35 #############"
git checkout -b b-r35 b-r8
git merge b-r33 --no-ff -m "Merge b-r33 into b-r35"
unzip -o "$CMTS/commit35.zip" -d .
git add .
git commit -m "r35" --author="red <red@example.com>"

##########################
# r36 – switch to b-r9 and commit commit36 with blue
##########################
echo "############# r36 #############"
git checkout b-r9
unzip -o "$CMTS/commit36.zip" -d .
git add .
git commit -m "r36" --author="blue <blue@example.com>"

##########################
# r37 – switch to b-r2 and merge in b-r9, then commit (simulate merge commit)
##########################
echo "############# r37 #############"
git checkout b-r2
git merge b-r9 --no-ff -m "r37" --author="blue <blue@example.com>"

##########################
# r38 – switch to b-r13 and commit commit38 with blue
##########################
echo "############# r38 #############"
git checkout b-r13
unzip -o "$CMTS/commit38.zip" -d .
git add .
git commit -m "r38" --author="blue <blue@example.com>"

##########################
# r39 – switch to b-r35 and commit commit39 with red
##########################
echo "############# r39 #############"
git checkout b-r35
unzip -o "$CMTS/commit39.zip" -d .
git add .
git commit -m "r39" --author="red <red@example.com>"

##########################
# r40 – switch to main, merge in b-r35, then commit commit40 with red
##########################
echo "############# r40 #############"
git checkout main
git merge b-r35 --no-ff -m "Merge b-r35 into main" \
  --author="red <red@example.com>"
unzip -o "$CMTS/commit40.zip" -d .
git add .
git commit -m "r40" --author="red <red@example.com>"

##########################
# r41 – switch to b-r30 and commit commit41 with red
##########################
echo "############# r41 #############"
git checkout b-r30
unzip -o "$CMTS/commit41.zip" -d .
git add .
git commit -m "r41" --author="red <red@example.com>"

##########################
# r42 – switch to b-r26 and commit commit42 with blue
##########################
echo "############# r42 #############"
git checkout b-r26
unzip -o "$CMTS/commit42.zip" -d .
git add .
git commit -m "r42" --author="blue <blue@example.com>"

##########################
# r43 – switch to b-r3, merge in b-r26, then commit with red
##########################
echo "############# r43 #############"
git checkout b-r3
git merge b-r26 --no-ff -m "r43" --author="red <red@example.com>"

##########################
# r44 – switch to b-r30 and commit commit44 with red
##########################
echo "############# r44 #############"
git checkout b-r30
unzip -o "$CMTS/commit44.zip" -d .
git add .
git commit -m "r44" --author="red <red@example.com>"

##########################
# r45 – switch to b-r19, merge in b-r30, then commit with red
##########################
echo "############# r45 #############"
git checkout b-r19
git merge b-r30 --no-ff -m "r45" --author="red <red@example.com>"

##########################
# r46 – switch to b-r11, merge in b-r19, then commit with red
##########################
echo "############# r46 #############"
git checkout b-r11
git merge b-r19 --no-ff -m "r46" --author="red <red@example.com>"

##########################
# r47 – (remaining on b-r11) commit commit47 with red
##########################
echo "############# r47 #############"
unzip -o "$CMTS/commit47.zip" -d .
git add .
git commit -m "r47" --author="red <red@example.com>"

##########################
# r48 – switch to b-r18 and commit commit48 with red
##########################
echo "############# r48 #############"
git checkout b-r18
unzip -o "$CMTS/commit48.zip" -d .
git add .
git commit -m "r48" --author="red <red@example.com>"

##########################
# r49 – still on b-r18, commit commit49 with red
##########################
echo "############# r49 #############"
unzip -o "$CMTS/commit49.zip" -d .
git add .
git commit -m "r49" --author="red <red@example.com>"

##########################
# r50 – switch to b-r28 and commit commit50 with red
##########################
echo "############# r50 #############"
git checkout b-r28
unzip -o "$CMTS/commit50.zip" -d .
git add .
git commit -m "r50" --author="red <red@example.com>"

##########################
# r51 – switch to b-r18, merge in b-r28, then commit with red
##########################
echo "############# r51 #############"
git checkout b-r18
git merge b-r28 --no-ff -m "r51" --author="red <red@example.com>"

##########################
# r52 – switch to b-r3 and commit commit52 with red
##########################
echo "############# r52 #############"
git checkout b-r3
unzip -o "$CMTS/commit52.zip" -d .
git add .
git commit -m "r52" --author="red <red@example.com>"

##########################
# r53 – switch to b-r14 and commit commit53 with blue
##########################
echo "############# r53 #############"
git checkout b-r14
unzip -o "$CMTS/commit53.zip" -d .
git add .
git commit -m "r53" --author="blue <blue@example.com>"

##########################
# r54 – switch to b-r24 and commit commit54 with red
##########################
echo "############# r54 #############"
git checkout b-r24
unzip -o "$CMTS/commit54.zip" -d .
git add .
git commit -m "r54" --author="red <red@example.com>"

##########################
# r55 – branch b-r55 from b-r24 and commit commit55 with blue
##########################
echo "############# r55 #############"
git checkout -b b-r55 b-r24
unzip -o "$CMTS/commit55.zip" -d .
git add .
git commit -m "r55" --author="blue <blue@example.com>"

##########################
# r56 – switch to b-r1 and commit commit56 with blue
##########################
echo "############# r56 #############"
git checkout b-r1
unzip -o "$CMTS/commit56.zip" -d .
git add .
git commit -m "r56" --author="blue <blue@example.com>"

##########################
# r57 – (still on b-r1) commit commit57 with blue
##########################
echo "############# r57 #############"
unzip -o "$CMTS/commit57.zip" -d .
git add .
git commit -m "r57" --author="blue <blue@example.com>"

##########################
# r58 – switch to b-r29 and commit commit58 with red
##########################
echo "############# r58 #############"
git checkout b-r29
unzip -o "$CMTS/commit58.zip" -d .
git add .
git commit -m "r58" --author="red <red@example.com>"

##########################
# r59 – switch to b-r1, merge in b-r29, then commit with blue
##########################
echo "############# r59 #############"
git checkout b-r1
git merge b-r29 --no-ff -m "r59" --author="blue <blue@example.com>"

##########################
# r60 – switch to b-r5, merge in b-r1, then commit with blue
##########################
echo "############# r60 #############"
git checkout b-r5
git merge b-r1 --no-ff -m "r60" --author="blue <blue@example.com>"

##########################
# r61 – switch to b-r2 and commit commit61 with blue
##########################
echo "############# r61 #############"
git checkout b-r2
unzip -o "$CMTS/commit61.zip" -d .
git add .
git commit -m "r61" --author="blue <blue@example.com>"

##########################
# r62 – branch b-r62 from b-r2 and commit commit62 with blue
##########################
echo "############# r62 #############"
git checkout -b b-r62 b-r2
unzip -o "$CMTS/commit62.zip" -d .
git add .
git commit -m "r62" --author="blue <blue@example.com>"

##########################
# r63 – switch to b-r11 and commit commit63 with red
##########################
echo "############# r63 #############"
git checkout b-r11
unzip -o "$CMTS/commit63.zip" -d .
git add .
git commit -m "r63" --author="red <red@example.com>"

##########################
# r64 – switch to b-r7 and commit commit64 with blue
##########################
echo "############# r64 #############"
git checkout b-r7
unzip -o "$CMTS/commit64.zip" -d .
git add .
git commit -m "r64" --author="blue <blue@example.com>"

##########################
# r65 – switch to b-r3 and commit commit65 with red
##########################
echo "############# r65 #############"
git checkout b-r3
unzip -o "$CMTS/commit65.zip" -d .
git add .
git commit -m "r65" --author="red <red@example.com>"

##########################
# r66 – switch to b-r62 and commit commit66 with blue
##########################
echo "############# r66 #############"
git checkout b-r62
unzip -o "$CMTS/commit66.zip" -d .
git add .
git commit -m "r66" --author="blue <blue@example.com>"

##########################
# r67 – switch to b-r11, merge in b-r62, then commit with red
##########################
echo "############# r67 #############"
git checkout b-r11
git merge b-r62 --no-ff -m "r67" --author="red <red@example.com>"

##########################
# r68 – switch to b-r7 and commit commit68 with blue
##########################
echo "############# r68 #############"
git checkout b-r7
unzip -o "$CMTS/commit68.zip" -d .
git add .
git commit -m "r68" --author="blue <blue@example.com>"

##########################
# r69 – (still on current branch) commit commit69 with blue
##########################
echo "############# r69 #############"
unzip -o "$CMTS/commit69.zip" -d .
git add .
git commit -m "r69" --author="blue <blue@example.com>"

##########################
# r70 – switch to b-r18 and commit commit70 with red
##########################
echo "############# r70 #############"
git checkout b-r18
unzip -o "$CMTS/commit70.zip" -d .
git add .
git commit -m "r70" --author="red <red@example.com>"

##########################
# r71 – switch to b-r3, merge in b-r18 and resolve conflict for eqkVEXsynE.PhR,
#      then commit with red
##########################
echo "############# r71 #############"
git checkout b-r3
# Start merge without auto committing to simulate conflict resolution
git merge b-r18 --no-commit --no-ff -m "r71" \
  --author="red <red@example.com>" || true
# (Assume a conflict arose in eqkVEXsynE.PhR; resolve it by taking our version)
git checkout --ours eqkVEXsynE.PhR
git add eqkVEXsynE.PhR
# Now finish the merge commit
git commit -m "r71" --author="red <red@example.com>"

##########################
# r72 – commit commit72 with red
##########################
echo "############# r72 #############"
unzip -o "$CMTS/commit72.zip" -d .
git add .
git commit -m "r72" --author="red <red@example.com>"

##########################
# r73 – switch to b-r10 and commit commit73 with blue
##########################
echo "############# r73 #############"
git checkout b-r10
unzip -o "$CMTS/commit73.zip" -d .
git add .
git commit -m "r73" --author="blue <blue@example.com>"

##########################
# r74 – switch to b-r18 and commit commit74 with red
##########################
echo "############# r74 #############"
git checkout b-r18
unzip -o "$CMTS/commit74.zip" -d .
git add .
git commit -m "r74" --author="red <red@example.com>"

##########################
# r75 – switch to b-r55, merge in b-r18, then commit with blue
##########################
echo "############# r75 #############"
git checkout b-r55
git merge b-r18 --no-ff -m "r75" --author="blue <blue@example.com>"

##########################
# r76 – switch to b-r2, merge in b-r55, then commit with blue
##########################
echo "############# r76 #############"
git checkout b-r2
git merge b-r55 --no-ff -m "r76" --author="blue <blue@example.com>"

##########################
# r77 – switch to b-r11, merge in b-r2, then commit with red
##########################
echo "############# r77 #############"
git checkout b-r11
git merge b-r2 --no-ff -m "r77" --author="red <red@example.com>"

##########################
# r78 – switch to b-r10, merge in b-r77, then commit with blue
##########################
echo "############# r78 #############"
git checkout b-r10
git merge b-r77 --no-ff -m "r78" --author="blue <blue@example.com>"

##########################
# r79 – switch to b-r13, merge in b-r10, then commit with blue
##########################
echo "############# r79 #############"
git checkout b-r13
git merge b-r10 --no-ff -m "r79" --author="blue <blue@example.com>"

##########################
# r80 – switch to b-r7, merge in b-r13, then commit with blue
##########################
echo "############# r80 #############"
git checkout b-r7
git merge b-r13 --no-ff -m "r80" --author="blue <blue@example.com>"

##########################
# r81 – switch to b-r5, merge in b-r7, then commit with blue
##########################
echo "############# r81 #############"
git checkout b-r5
git merge b-r7 --no-ff -m "r81" --author="blue <blue@example.com>"

##########################
# r82 – switch to b-r24, merge in b-r5, then commit with red
##########################
echo "############# r82 #############"
git checkout b-r24
git merge b-r5 --no-ff -m "r82" --author="red <red@example.com>"

##########################
# r83 – switch to b-r20, merge in b-r24, then commit with red
##########################
echo "############# r83 #############"
git checkout b-r20
git merge b-r24 --no-ff -m "r83" --author="red <red@example.com>"

##########################
# r84 – switch to b-r3, merge in b-r20, then commit with red
##########################
echo "############# r84 #############"
git checkout b-r3
git merge b-r20 --no-ff -m "r84" --author="red <red@example.com>"

##########################
# r85 – switch to main, merge in b-r3, then commit with red
##########################
echo "############# r85 #############"
git checkout main
git merge b-r3 --no-ff -m "r85" --author="red <red@example.com>"

echo "All revisions have been processed in Git."
