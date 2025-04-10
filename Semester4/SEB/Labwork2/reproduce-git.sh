#!/bin/bash
set -ex

# Path to the directory containing commit*.zip files
CMTS="$(realpath commits)"

# Create a new Git repository, using "main" as the trunk.
mkdir repo && cd repo
git init -b main

#------------------------------------------------------------------------------
# Helper functions
#------------------------------------------------------------------------------
# commit_changes: stage a commit using --allow-empty so that even if there
# are no staged changes, a commit will be created.
commit_changes() {
  local msg="$1"
  local author="$2"
  git commit --allow-empty -m "$msg" --author "$author"
}

# git_merge_with_empty: merge in another branch while forcing an empty merge
# commit if nothing new is merged (to mimic SVN’s always‐recorded commit).
git_merge_with_empty() {
  local branch="$1"
  local msg="$2"
  local author="$3"
  local old_head new_head

  old_head=$(git rev-parse HEAD)
  # If the merge succeeds it will produce a merge commit.
  git merge "$branch" --no-ff -m "$msg" -X theirs || true
  new_head=$(git rev-parse HEAD)
  if [ "$old_head" = "$new_head" ]; then
    echo "Merge of $branch produced no changes; forcing empty merge commit."
    git commit --allow-empty -m "$msg"
  fi
}

#------------------------------------------------------------------------------
# Revision-by-revision conversion
# (The SVN "copy" becomes a branch creation; "switch" becomes checkout;
# and each commit uses --allow-empty so that even if the working tree has no
# modifications the commit is recorded.)
#------------------------------------------------------------------------------

##########################
# r0 – initial commit on main (trunk)
##########################
echo "############# r0 #############"
rm -rf *
unzip -o "$CMTS/commit0.zip" -d .
git add .
commit_changes "r0" "red <red@example.com>"

##########################
# r1 – create branch b-r1 from main; remove all files and commit commit1 with blue
##########################
echo "############# r1 #############"
git checkout -b b-r1 main
git rm -r --ignore-unmatch .
rm -rf *
unzip -o "$CMTS/commit1.zip" -d .
git add .
commit_changes "r1" "blue <blue@example.com>"

##########################
# r2 – branch b-r2 from b-r1 and commit commit2 with blue
##########################
echo "############# r2 #############"
git checkout -b b-r2 b-r1
unzip -o "$CMTS/commit2.zip" -d .
git add .
commit_changes "r2" "blue <blue@example.com>"

##########################
# r3 – branch b-r3 from b-r2 and commit commit3 with red
##########################
echo "############# r3 #############"
git checkout -b b-r3 b-r2
unzip -o "$CMTS/commit3.zip" -d .
git add .
commit_changes "r3" "red <red@example.com>"

##########################
# r4 – branch b-r4 from b-r3 and commit commit4 with red
##########################
echo "############# r4 #############"
git checkout -b b-r4 b-r3
unzip -o "$CMTS/commit4.zip" -d .
git add .
commit_changes "r4" "red <red@example.com>"

##########################
# r5 – branch b-r5 from b-r4, commit commit5 with blue, then merge in b-r4
##########################
echo "############# r5 #############"
git checkout -b b-r5 b-r4
unzip -o "$CMTS/commit5.zip" -d .
git add .
commit_changes "r5" "blue <blue@example.com>"
git_merge_with_empty "b-r4" "Merge b-r4 into b-r5" "blue <blue@example.com>"

##########################
# r6 – switch back to b-r3 and commit commit6 with red
##########################
echo "############# r6 #############"
git checkout b-r3
unzip -o "$CMTS/commit6.zip" -d .
git add .
commit_changes "r6" "red <red@example.com>"

##########################
# r7 – branch b-r7 from b-r3 and commit commit7 with blue
##########################
echo "############# r7 #############"
git checkout -b b-r7 b-r3
unzip -o "$CMTS/commit7.zip" -d .
git add .
commit_changes "r7" "blue <blue@example.com>"

##########################
# r8 – branch b-r8 from b-r7 and commit commit8 with red
##########################
echo "############# r8 #############"
git checkout -b b-r8 b-r7
unzip -o "$CMTS/commit8.zip" -d .
git add .
commit_changes "r8" "red <red@example.com>"

##########################
# r9 – branch b-r9 from b-r8 and commit commit9 with blue
##########################
echo "############# r9 #############"
git checkout -b b-r9 b-r8
unzip -o "$CMTS/commit9.zip" -d .
git add .
commit_changes "r9" "blue <blue@example.com>"

##########################
# r10 – branch b-r10 from b-r9 and commit commit10 with blue
##########################
echo "############# r10 #############"
git checkout -b b-r10 b-r9
unzip -o "$CMTS/commit10.zip" -d .
git add .
commit_changes "r10" "blue <blue@example.com>"

##########################
# r11 – branch b-r11 from b-r10 and commit commit11 with red
##########################
echo "############# r11 #############"
git checkout -b b-r11 b-r10
unzip -o "$CMTS/commit11.zip" -d .
git add .
commit_changes "r11" "red <red@example.com>"

##########################
# r12 – switch to b-r1 and commit commit12 with blue
##########################
echo "############# r12 #############"
git checkout b-r1
unzip -o "$CMTS/commit12.zip" -d .
git add .
commit_changes "r12" "blue <blue@example.com>"

##########################
# r13 – branch b-r13 from b-r1 and commit commit13 with blue
##########################
echo "############# r13 #############"
git checkout -b b-r13 b-r1
unzip -o "$CMTS/commit13.zip" -d .
git add .
commit_changes "r13" "blue <blue@example.com>"

##########################
# r14 – branch b-r14 from b-r13 and commit commit14 with blue
##########################
echo "############# r14 #############"
git checkout -b b-r14 b-r13
unzip -o "$CMTS/commit14.zip" -d .
git add .
commit_changes "r14" "blue <blue@example.com>"

##########################
# r15 – branch b-r15 from b-r14 and commit commit15 with red
##########################
echo "############# r15 #############"
git checkout -b b-r15 b-r14
unzip -o "$CMTS/commit15.zip" -d .
git add .
commit_changes "r15" "red <red@example.com>"

##########################
# r16 – branch b-r16 from b-r15, commit commit16 with blue, then merge in b-r15
##########################
echo "############# r16 #############"
git checkout -b b-r16 b-r15
unzip -o "$CMTS/commit16.zip" -d .
git add .
commit_changes "r16" "blue <blue@example.com>"
git_merge_with_empty "b-r15" "Merge b-r15 into b-r16" "blue <blue@example.com>"

##########################
# r17 – switch to b-r2 and commit commit17 with blue
##########################
echo "############# r17 #############"
git checkout b-r2
unzip -o "$CMTS/commit17.zip" -d .
git add .
commit_changes "r17" "blue <blue@example.com>"

##########################
# r18 – branch b-r18 from b-r2 and commit commit18 with red
##########################
echo "############# r18 #############"
git checkout -b b-r18 b-r2
unzip -o "$CMTS/commit18.zip" -d .
git add .
commit_changes "r18" "red <red@example.com>"

##########################
# r19 – branch b-r19 from b-r18 and commit commit19 with blue
##########################
echo "############# r19 #############"
git checkout -b b-r19 b-r18
unzip -o "$CMTS/commit19.zip" -d .
git add .
commit_changes "r19" "blue <blue@example.com>"

##########################
# r20 – branch b-r20 from b-r19 and commit commit20 with red
##########################
echo "############# r20 #############"
git checkout -b b-r20 b-r19
unzip -o "$CMTS/commit20.zip" -d .
git add .
commit_changes "r20" "red <red@example.com>"

##########################
# r21 – switch to b-r16 and commit commit21 with blue
##########################
echo "############# r21 #############"
git checkout b-r16
unzip -o "$CMTS/commit21.zip" -d .
git add .
commit_changes "r21" "blue <blue@example.com>"

##########################
# r22 – switch to b-r13 and commit commit22 with blue
##########################
echo "############# r22 #############"
git checkout b-r13
unzip -o "$CMTS/commit22.zip" -d .
git add .
commit_changes "r22" "blue <blue@example.com>"

##########################
# r23 – switch to b-r5 and commit commit23 with blue
##########################
echo "############# r23 #############"
git checkout b-r5
unzip -o "$CMTS/commit23.zip" -d .
git add .
commit_changes "r23" "blue <blue@example.com>"

##########################
# r24 – branch b-r24 from b-r5 and commit commit24 with red
##########################
echo "############# r24 #############"
git checkout -b b-r24 b-r5
unzip -o "$CMTS/commit24.zip" -d .
git add .
commit_changes "r24" "red <red@example.com>"

##########################
# r25 – switch to b-r1 and commit commit25 with blue
##########################
echo "############# r25 #############"
git checkout b-r1
unzip -o "$CMTS/commit25.zip" -d .
git add .
commit_changes "r25" "blue <blue@example.com>"

##########################
# r26 – branch b-r26 from b-r1 and commit commit26 with blue
##########################
echo "############# r26 #############"
git checkout -b b-r26 b-r1
unzip -o "$CMTS/commit26.zip" -d .
git add .
commit_changes "r26" "blue <blue@example.com>"

##########################
# r27 – switch to b-r16 and commit commit27 with blue
##########################
echo "############# r27 #############"
git checkout b-r16
unzip -o "$CMTS/commit27.zip" -d .
git add .
commit_changes "r27" "blue <blue@example.com>"

##########################
# r28 – branch b-r28 from b-r16, commit commit28 with red, and merge in b-r16
##########################
echo "############# r28 #############"
git checkout -b b-r28 b-r16
unzip -o "$CMTS/commit28.zip" -d .
git add .
commit_changes "r28" "red <red@example.com>"
git_merge_with_empty "b-r16" "Merge b-r16 into b-r28" "red <red@example.com>"

##########################
# r29 – branch b-r29 from b-r28 and commit commit29 with red
##########################
echo "############# r29 #############"
git checkout -b b-r29 b-r28
unzip -o "$CMTS/commit29.zip" -d .
git add .
commit_changes "r29" "red <red@example.com>"

##########################
# r30 – branch b-r30 from b-r29 and commit commit30 with red
##########################
echo "############# r30 #############"
git checkout -b b-r30 b-r29
unzip -o "$CMTS/commit30.zip" -d .
git add .
commit_changes "r30" "red <red@example.com>"

##########################
# r31 – switch to b-r5 and commit commit31 with blue
##########################
echo "############# r31 #############"
git checkout b-r5
unzip -o "$CMTS/commit31.zip" -d .
git add .
commit_changes "r31" "blue <blue@example.com>"

##########################
# r32 – switch to b-r30 and commit commit32 with red
##########################
echo "############# r32 #############"
git checkout b-r30
unzip -o "$CMTS/commit32.zip" -d .
git add .
commit_changes "r32" "red <red@example.com>"

##########################
# r33 – branch b-r33 from b-r30 and commit commit33 with red
##########################
echo "############# r33 #############"
git checkout -b b-r33 b-r30
unzip -o "$CMTS/commit33.zip" -d .
git add .
commit_changes "r33" "red <red@example.com>"

##########################
# r34 – switch to b-r8 and commit commit34 with red
##########################
echo "############# r34 #############"
git checkout b-r8
unzip -o "$CMTS/commit34.zip" -d .
git add .
commit_changes "r34" "red <red@example.com>"

##########################
# r35 – branch b-r35 from b-r8, merge in b-r33, then commit commit35 with red
##########################
echo "############# r35 #############"
git checkout -b b-r35 b-r8
git_merge_with_empty "b-r33" "Merge b-r33 into b-r35" "red <red@example.com>"
unzip -o "$CMTS/commit35.zip" -d .
git add .
commit_changes "r35" "red <red@example.com>"

##########################
# r36 – switch to b-r9 and commit commit36 with blue
##########################
echo "############# r36 #############"
git checkout b-r9
unzip -o "$CMTS/commit36.zip" -d .
git add .
commit_changes "r36" "blue <blue@example.com>"

##########################
# r37 – switch to b-r2, merge in b-r9, then record the merge as r37 with blue
##########################
echo "############# r37 #############"
git checkout b-r2
git_merge_with_empty "b-r9" "r37" "blue <blue@example.com>"

##########################
# r38 – switch to b-r13 and commit commit38 with blue
##########################
echo "############# r38 #############"
git checkout b-r13
unzip -o "$CMTS/commit38.zip" -d .
git add .
commit_changes "r38" "blue <blue@example.com>"

##########################
# r39 – switch to b-r35 and commit commit39 with red
##########################
echo "############# r39 #############"
git checkout b-r35
unzip -o "$CMTS/commit39.zip" -d .
git add .
commit_changes "r39" "red <red@example.com>"

##########################
# r40 – switch to main, merge in b-r35, then commit commit40 with red
##########################
echo "############# r40 #############"
git checkout main
git_merge_with_empty "b-r35" "Merge b-r35 into main" "red <red@example.com>"
unzip -o "$CMTS/commit40.zip" -d .
git add .
commit_changes "r40" "red <red@example.com>"

##########################
# r41 – switch to b-r30 and commit commit41 with red
##########################
echo "############# r41 #############"
git checkout b-r30
unzip -o "$CMTS/commit41.zip" -d .
git add .
commit_changes "r41" "red <red@example.com>"

##########################
# r42 – switch to b-r26 and commit commit42 with blue
##########################
echo "############# r42 #############"
git checkout b-r26
unzip -o "$CMTS/commit42.zip" -d .
git add .
commit_changes "r42" "blue <blue@example.com>"

##########################
# r43 – switch to b-r3, merge in b-r26, then record merge as r43 with red
##########################
echo "############# r43 #############"
git checkout b-r3
git_merge_with_empty "b-r26" "r43" "red <red@example.com>"

##########################
# r44 – switch to b-r30 and commit commit44 with red
##########################
echo "############# r44 #############"
git checkout b-r30
unzip -o "$CMTS/commit44.zip" -d .
git add .
commit_changes "r44" "red <red@example.com>"

##########################
# r45 – switch to b-r19, merge in b-r30, then record merge as r45 with red
##########################
echo "############# r45 #############"
git checkout b-r19
git_merge_with_empty "b-r30" "r45" "red <red@example.com>"

##########################
# r46 – switch to b-r11, merge in b-r19, then record merge as r46 with red
##########################
echo "############# r46 #############"
git checkout b-r11
git_merge_with_empty "b-r19" "r46" "red <red@example.com>"

##########################
# r47 – commit commit47 with red (still on b-r11)
##########################
echo "############# r47 #############"
unzip -o "$CMTS/commit47.zip" -d .
git add .
commit_changes "r47" "red <red@example.com>"

##########################
# r48 – switch to b-r18 and commit commit48 with red
##########################
echo "############# r48 #############"
git checkout b-r18
unzip -o "$CMTS/commit48.zip" -d .
git add .
commit_changes "r48" "red <red@example.com>"

##########################
# r49 – (remaining on b-r18) commit commit49 with red
##########################
echo "############# r49 #############"
unzip -o "$CMTS/commit49.zip" -d .
git add .
commit_changes "r49" "red <red@example.com>"

##########################
# r50 – switch to b-r28 and commit commit50 with red
##########################
echo "############# r50 #############"
git checkout b-r28
unzip -o "$CMTS/commit50.zip" -d .
git add .
commit_changes "r50" "red <red@example.com>"

##########################
# r51 – switch to b-r18, merge in b-r28, then record merge as r51 with red
##########################
echo "############# r51 #############"
git checkout b-r18
git_merge_with_empty "b-r28" "r51" "red <red@example.com>"

##########################
# r52 – switch to b-r3 and commit commit52 with red
##########################
echo "############# r52 #############"
git checkout b-r3
unzip -o "$CMTS/commit52.zip" -d .
git add .
commit_changes "r52" "red <red@example.com>"

##########################
# r53 – switch to b-r14 and commit commit53 with blue
##########################
echo "############# r53 #############"
git checkout b-r14
unzip -o "$CMTS/commit53.zip" -d .
git add .
commit_changes "r53" "blue <blue@example.com>"

##########################
# r54 – switch to b-r24 and commit commit54 with red
##########################
echo "############# r54 #############"
git checkout b-r24
unzip -o "$CMTS/commit54.zip" -d .
git add .
commit_changes "r54" "red <red@example.com>"

##########################
# r55 – branch b-r55 from b-r24 and commit commit55 with blue
##########################
echo "############# r55 #############"
git checkout -b b-r55 b-r24
unzip -o "$CMTS/commit55.zip" -d .
git add .
commit_changes "r55" "blue <blue@example.com>"

##########################
# r56 – switch to b-r1 and commit commit56 with blue
##########################
echo "############# r56 #############"
git checkout b-r1
unzip -o "$CMTS/commit56.zip" -d .
git add .
commit_changes "r56" "blue <blue@example.com>"

##########################
# r57 – (still on b-r1) commit commit57 with blue
##########################
echo "############# r57 #############"
unzip -o "$CMTS/commit57.zip" -d .
git add .
commit_changes "r57" "blue <blue@example.com>"

##########################
# r58 – switch to b-r29 and commit commit58 with red
##########################
echo "############# r58 #############"
git checkout b-r29
unzip -o "$CMTS/commit58.zip" -d .
git add .
commit_changes "r58" "red <red@example.com>"

##########################
# r59 – switch to b-r1, merge in b-r29, then record merge as r59 with blue
##########################
echo "############# r59 #############"
git checkout b-r1
git_merge_with_empty "b-r29" "r59" "blue <blue@example.com>"

##########################
# r60 – switch to b-r5, merge in b-r1, then record merge as r60 with blue
##########################
echo "############# r60 #############"
git checkout b-r5
git_merge_with_empty "b-r1" "r60" "blue <blue@example.com>"

##########################
# r61 – switch to b-r2 and commit commit61 with blue
##########################
echo "############# r61 #############"
git checkout b-r2
unzip -o "$CMTS/commit61.zip" -d .
git add .
commit_changes "r61" "blue <blue@example.com>"

##########################
# r62 – branch b-r62 from b-r2 and commit commit62 with blue
##########################
echo "############# r62 #############"
git checkout -b b-r62 b-r2
unzip -o "$CMTS/commit62.zip" -d .
git add .
commit_changes "r62" "blue <blue@example.com>"

##########################
# r63 – switch to b-r11 and commit commit63 with red
##########################
echo "############# r63 #############"
git checkout b-r11
unzip -o "$CMTS/commit63.zip" -d .
git add .
commit_changes "r63" "red <red@example.com>"

##########################
# r64 – switch to b-r7 and commit commit64 with blue
##########################
echo "############# r64 #############"
git checkout b-r7
unzip -o "$CMTS/commit64.zip" -d .
git add .
commit_changes "r64" "blue <blue@example.com>"

##########################
# r65 – switch to b-r3 and commit commit65 with red
##########################
echo "############# r65 #############"
git checkout b-r3
unzip -o "$CMTS/commit65.zip" -d .
git add .
commit_changes "r65" "red <red@example.com>"

##########################
# r66 – switch to b-r62 and commit commit66 with blue
##########################
echo "############# r66 #############"
git checkout b-r62
unzip -o "$CMTS/commit66.zip" -d .
git add .
commit_changes "r66" "blue <blue@example.com>"

##########################
# r67 – switch to b-r11, merge in b-r62, then record merge as r67 with red
##########################
echo "############# r67 #############"
git checkout b-r11
git_merge_with_empty "b-r62" "r67" "red <red@example.com>"

##########################
# r68 – switch to b-r7 and commit commit68 with blue
##########################
echo "############# r68 #############"
git checkout b-r7
unzip -o "$CMTS/commit68.zip" -d .
git add .
commit_changes "r68" "blue <blue@example.com>"

##########################
# r69 – (still on current branch) commit commit69 with blue
##########################
echo "############# r69 #############"
unzip -o "$CMTS/commit69.zip" -d .
git add .
commit_changes "r69" "blue <blue@example.com>"

##########################
# r70 – switch to b-r18 and commit commit70 with red
##########################
echo "############# r70 #############"
git checkout b-r18
unzip -o "$CMTS/commit70.zip" -d .
git add .
commit_changes "r70" "red <red@example.com>"

##########################
# r71 – switch to b-r3, merge in b-r18 and resolve a simulated conflict,
# then finish the merge by committing with red.
##########################
echo "############# r71 #############"
git checkout b-r3
# Start merge without auto-committing to simulate conflict resolution.
git merge b-r18 --no-commit --no-ff -X theirs || true
# (Assume a conflict arose in eqkVEXsynE.PhR; resolve it by taking our version.)
git checkout --ours eqkVEXsynE.PhR
git add eqkVEXsynE.PhR
git commit --allow-empty -m "r71" --author="red <red@example.com>"

##########################
# r72 – commit commit72 with red
##########################
echo "############# r72 #############"
unzip -o "$CMTS/commit72.zip" -d .
git add .
commit_changes "r72" "red <red@example.com>"

##########################
# r73 – switch to b-r10 and commit commit73 with blue
##########################
echo "############# r73 #############"
git checkout b-r10
unzip -o "$CMTS/commit73.zip" -d .
git add .
commit_changes "r73" "blue <blue@example.com>"

##########################
# r74 – switch to b-r18 and commit commit74 with red
##########################
echo "############# r74 #############"
git checkout b-r18
unzip -o "$CMTS/commit74.zip" -d .
git add .
commit_changes "r74" "red <red@example.com>"

##########################
# r75 – switch to b-r55, merge in b-r18, then record merge as r75 with blue
##########################
echo "############# r75 #############"
git checkout b-r55
git_merge_with_empty "b-r18" "r75" "blue <blue@example.com>"

##########################
# r76 – switch to b-r2, merge in b-r55, then record merge as r76 with blue
##########################
echo "############# r76 #############"
git checkout b-r2
git_merge_with_empty "b-r55" "r76" "blue <blue@example.com>"

##########################
# r77 – switch to b-r11, merge in b-r2, then record merge as r77 with red
##########################
echo "############# r77 #############"
git checkout b-r11
git_merge_with_empty "b-r2" "r77" "red <red@example.com>"

##########################
# r78 – switch to b-r10, merge in b-r77, then record merge as r78 with blue
##########################
echo "############# r78 #############"
git checkout b-r10
git_merge_with_empty "b-r77" "r78" "blue <blue@example.com>"

##########################
# r79 – switch to b-r13, merge in b-r10, then record merge as r79 with blue
##########################
echo "############# r79 #############"
git checkout b-r13
git_merge_with_empty "b-r10" "r79" "blue <blue@example.com>"

##########################
# r80 – switch to b-r7, merge in b-r13, then record merge as r80 with blue
##########################
echo "############# r80 #############"
git checkout b-r7
git_merge_with_empty "b-r13" "r80" "blue <blue@example.com>"

##########################
# r81 – switch to b-r5, merge in b-r7, then record merge as r81 with blue
##########################
echo "############# r81 #############"
git checkout b-r5
git_merge_with_empty "b-r7" "r81" "blue <blue@example.com>"

##########################
# r82 – switch to b-r24, merge in b-r5, then record merge as r82 with red
##########################
echo "############# r82 #############"
git checkout b-r24
git_merge_with_empty "b-r5" "r82" "red <red@example.com>"

##########################
# r83 – switch to b-r20, merge in b-r24, then record merge as r83 with red
##########################
echo "############# r83 #############"
git checkout b-r20
git_merge_with_empty "b-r24" "r83" "red <red@example.com>"

##########################
# r84 – switch to b-r3, merge in b-r20, then record merge as r84 with red
##########################
echo "############# r84 #############"
git checkout b-r3
git_merge_with_empty "b-r20" "r84" "red <red@example.com>"

##########################
# r85 – switch to main, merge in b-r3, then record merge as r85 with red
##########################
echo "############# r85 #############"
git checkout main
git_merge_with_empty "b-r3" "r85" "red <red@example.com>"

echo "All revisions have been processed in Git."
