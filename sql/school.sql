/*
 Navicat MySQL Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : localhost:3306
 Source Schema         : optlog

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 20/11/2020 18:12:17
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS optlog.`course`;
CREATE TABLE optlog.`course`  (
  `course_num` int(0) NOT NULL COMMENT '课程号',
  `course_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '课程名',
  `course_hour` int(0) NOT NULL COMMENT '课程学时',
  `course_score` int(0) NOT NULL COMMENT '课程学分',
  PRIMARY KEY (`course_num`) USING BTREE,
  INDEX `course_num`(`course_num`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci COMMENT = '课程信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO optlog.`course` VALUES (1, '概率论', 5, 2);
INSERT INTO optlog.`course` VALUES (2, 'MySQL', 5, 2);
INSERT INTO optlog.`course` VALUES (3, '英语', 5, 3);
INSERT INTO optlog.`course` VALUES (4, '毛概', 2, 5);

-- ----------------------------
-- Table structure for score
-- ----------------------------
DROP TABLE IF EXISTS optlog.`score`;
CREATE TABLE optlog.`score`  (
  `score_id` int(0) NOT NULL,
  `course_num` int(0) NOT NULL,
  `student_num` int(0) NOT NULL,
  `score` int(0) NOT NULL COMMENT '分数',
  PRIMARY KEY (`score_id`) USING BTREE,
  INDEX `course_num`(`course_num`) USING BTREE,
  INDEX `student_num`(`student_num`) USING BTREE,
  CONSTRAINT `score_ibfk_1` FOREIGN KEY (`course_num`) REFERENCES `course` (`course_num`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `score_ibfk_2` FOREIGN KEY (`student_num`) REFERENCES `student` (`student_num`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci COMMENT = '学生成绩表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of score
-- ----------------------------
INSERT INTO optlog.`score` VALUES (1, 1, 15001, 89);
INSERT INTO optlog.`score` VALUES (2, 1, 15002, 78);
INSERT INTO optlog.`score` VALUES (3, 1, 15003, 80);
INSERT INTO optlog.`score` VALUES (4, 1, 16004, 78);
INSERT INTO optlog.`score` VALUES (5, 2, 15001, 85);
INSERT INTO optlog.`score` VALUES (6, 2, 15002, 78);
INSERT INTO optlog.`score` VALUES (7, 2, 15003, 75);
INSERT INTO optlog.`score` VALUES (8, 2, 16004, 89);
INSERT INTO optlog.`score` VALUES (9, 3, 15001, 87);
INSERT INTO optlog.`score` VALUES (10, 3, 15002, 77);
INSERT INTO optlog.`score` VALUES (11, 3, 15003, 88);
INSERT INTO optlog.`score` VALUES (12, 3, 16004, 90);
INSERT INTO optlog.`score` VALUES (13, 4, 15001, 90);
INSERT INTO optlog.`score` VALUES (14, 4, 15002, 98);
INSERT INTO optlog.`score` VALUES (15, 4, 15003, 89);
INSERT INTO optlog.`score` VALUES (16, 4, 16004, 88);

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS optlog.`student`;
CREATE TABLE optlog.`student`  (
  `student_num` int(0) NOT NULL COMMENT '学生学号',
  `student_name` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '学生姓名',
  `student_sex` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '男' COMMENT '学生性别',
  `student_birthday` date NOT NULL COMMENT '学生生日',
  PRIMARY KEY (`student_num`) USING BTREE,
  INDEX `student_num`(`student_num`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci COMMENT = '学生基本信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO optlog.`student` VALUES (15001, 'Mark', '男', '1997-02-19');
INSERT INTO optlog.`student` VALUES (15002, 'Wen', '男', '1997-09-16');
INSERT INTO optlog.`student` VALUES (15003, 'Lee', '女', '1997-03-12');
INSERT INTO optlog.`student` VALUES (16004, 'Mary', '女', '1996-07-12');

SET FOREIGN_KEY_CHECKS = 1;
