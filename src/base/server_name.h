//
// server_name.h
// Copyright (C) 2020 4paradigm.com
// Author wangbao
// Date 2020-07-13
//

#pragma once

#include <gflags/gflags.h>
#include <string>
#include <iostream>
#include <fstream>
#include "base/file_util.h"
#include "base/id_generator.h"
#include "base/glog_wapper.h"

DECLARE_string(data_dir);

namespace rtidb {
namespace base {

bool WriteTxt(const std::string& full_path,
        std::string* name) {
    std::ofstream my_cout(full_path);
    if (my_cout.fail()) {
        PDLOG(WARNING, "init ofstream failed, path %s",
                full_path.data());
        return false;
    }
    IdGenerator ig;
    int64_t id = ig.Next();
    *name = std::to_string(id);
    my_cout << *name << std::endl;
    my_cout.close();
    return true;
}

bool ReadTxt(const std::string& full_path,
        std::string* name) {
    std::ifstream infile(full_path);
    if (infile.fail()) {
        PDLOG(WARNING, "init ifstream failed, path %s",
                full_path.data());
        return false;
    }
    getline(infile, *name);
    infile.close();
    return true;
}

bool GetNameFromTxt(std::string* name) {
    if (!IsExists(FLAGS_data_dir)) {
        if (!MkdirRecur(FLAGS_data_dir)) {
            PDLOG(WARNING, "make dir failed, path %s",
                    FLAGS_data_dir.data());
            return false;
        }
    }
    std::string full_path = FLAGS_data_dir + "/name.txt";
    if (!IsExists(full_path)) {
        return WriteTxt(full_path, name);
    }
    return ReadTxt(full_path, name);
}

}  // namespace base
}  // namespace rtidb
