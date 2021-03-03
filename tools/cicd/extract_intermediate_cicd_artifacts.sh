#!/bin/bash
ROOT_DIR=$(cd $(dirname $0); pwd)/../..
# tools/cicd/extract_intermediate_cicd_artifacts.sh
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

SUFFIX=$1
TAR_NAME="intermediate_cicd_artifact_${SUFFIX}.tar.gz"

# extract into java/fesql-proto java/fesql-native etc
cd ${ROOT_DIR}; tar xf ${TAR_NAME}
