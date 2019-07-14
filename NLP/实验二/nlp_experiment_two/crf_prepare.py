import os
import numpy as np

entity_tag_list = ['t', 'nr', 'ns', 'nt']
train_data_path = "./data/train.data"
test_data_path = "./data/test.data"
model_path = "./data/model"

args = {
    "template_path": "./data/template",
    "train_data_path": train_data_path,
    "model_path": model_path,
    "test_data_path": test_data_path,
    "output_path": "./data/output.txt"
}


def sentence_convert(sentence):
    ret_arr = []
    while sentence:
        pair = sentence.pop()
        if pair[1].endswith("]nt"):
            cur = pair
            pre = sentence.pop()
            while not pre[0].startswith("["):
                word = pre[0] + cur[0]
                cur = (word, "]nt")
                pre = sentence.pop()
            word = pre[0][1:] + cur[0]
            cur = (word, 'nt')
            sentence.append(cur)
            continue
        if pair[1] in entity_tag_list:
            ret_arr.append((pair[0], pair[1], pair[1]))
        else:
            ret_arr.append(pair)
    return ret_arr


def crf_data_gen(dict_path):
    crf_sentences = []
    assert os.path.exists(dict_path), "{path} 该地址不存在！".format_map({"path": dict_path})
    with open(dict_path, "r", encoding="utf-8") as file:
        for line in file:
            if line.strip() == '':
                continue
            try:
                sentences_pairs = [tuple(pair.split('/')) for pair in line.strip().split("  ")[1:]]
                has_entity = False
                for pair in sentences_pairs:
                    if pair[1] in entity_tag_list:
                        has_entity = True
                if not has_entity:
                    continue
                convert_ret = sentence_convert(sentences_pairs)
                if convert_ret:
                    convert_ret.reverse()
                    crf_sentences.append(convert_ret)
            except Exception as e:
                print(e)
                continue
    return crf_sentences


def write_to_file(path, data, model=2):
    sentence_end_tag = ["，", "。", "？", "；", "！"]
    with open(path, "w", encoding="utf-8") as f:
        for line in data:
            for pair in line:
                if pair[0] in sentence_end_tag:
                    print(file=f)
                else:
                    if model == 2:
                        print("{0}\t{1}".format(*pair), file=f)
                    else:
                        print("{0}\t{1}\t{2}".format(*pair), file=f)

            if line[-1][0] not in sentence_end_tag:
                print(file=f)


def gen_data_file(data, train_path, test_path, test_ratio=0.2, model=2):
    rule = {'t': "TIME", 'ns': 'LOC', 'nt': 'ORG', 'nr': 'NAME'}

    crf_train_data = []
    for sentence in data:
        temp_arr = []
        try:
            for pair in sentence:
                if len(pair) == 3:
                    suffix = rule[pair[1]]
                    if model == 2:
                        temp_arr.append((pair[0][0], "B_" + suffix))
                    else:
                        temp_arr.append((pair[0][0], pair[1], "B_" + suffix))
                    for ch in pair[0][1:-1]:
                        if model == 2:
                            temp_arr.append((ch, "M_" + suffix))
                        else:
                            temp_arr.append((ch, pair[1], "M_" + suffix))
                    if model == 2:
                        temp_arr.append((pair[0][-1], "E_" + suffix))
                    else:
                        temp_arr.append((pair[0][-1], pair[1], "E_" + suffix))
                else:
                    for ch in pair[0]:
                        if model == 2:
                            temp_arr.append((ch, 'O'))
                        else:
                            temp_arr.append((ch, pair[1], 'O'))
        except Exception as e:
            print(e)
            temp_arr = []
            continue
        if temp_arr:
            crf_train_data.append(temp_arr)
    np.random.shuffle(crf_train_data)
    train_size = int(len(crf_train_data) * (1 - test_ratio))
    train_data = crf_train_data[:train_size]
    test_data = crf_train_data[train_size:]
    write_to_file(train_path, train_data, model)
    write_to_file(test_path, test_data, model)


def train_crf(length):
    data_size = int(length * 0.7)
    crf_data = crf_data_gen("./data/train.txt")
    np.random.shuffle(crf_data)
    gen_data_file(crf_data[:data_size], train_data_path, test_data_path)
    os.system("crf_learn -a MIRA {template_path} {train_data_path} {model_path}".format_map(args))


def test_crf():
    os.system("crf_test -m {model_path} {test_data_path} >> {output_path}".format_map(args))
    real_tag_count = 0
    predict_tag_count = 0
    inner_count = 0

    with open(args['output_path'], "r", encoding="utf-8") as f:
        for line in f:
            if line.strip() == "":
                continue
            word, y, y_predict = line.strip().split("\t")
            if y != 'O':
                real_tag_count += 1
            if y_predict != 'O':
                predict_tag_count += 1
            if y == y_predict and y != 'O' and y_predict != 'O':
                inner_count += 1
    acc = inner_count / predict_tag_count
    recall = inner_count / real_tag_count
    f1_score = (2 * (acc * recall)) / (acc + recall)
    print("准确率: ", acc)
    print("召回率: ", recall)
    print("f值: ", f1_score)




if __name__ == '__main__':
    print('输入训练数据的大小')
    length = int(input())
    train_crf(length)#训练出模型
    test_crf()#测试得到结果


