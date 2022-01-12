#! /bin/bash

# Copyright 2021 4Paradigm
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

ulimit -c unlimited
ulimit -n 655360

cd "$(dirname "$0")/../" || exit

if [ -f "./conf/taskmanager.properties" ]; then
  cp ./conf/taskmanager.properties ./taskmanager/conf/taskmanager.properties
fi

pushd ./taskmanager/bin/ > /dev/null || exit
  sh ./taskmanager.sh
popd > /dev/null || exit