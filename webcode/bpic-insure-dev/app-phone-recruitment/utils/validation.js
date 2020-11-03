var validation = validation || {};

!function ($, e) {

    /* 验证是否为空 */
    e.isNull = function (val) {
        return ($.trim(val) === '');
    };

    /* 验证日期 2010-1-1 */
    e.date = function (val) {
        return (/^[1-9]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$/.test(val));
    };

    /* 验证时间 12:23:42 */
    e.time = function (val) {
        return (/^(20|21|22|23|[0-1]\d):[0-5]\d:[0-5]\d$/.test(val));
    };

    /* 银行卡卡号验证 */
    e.bankNoValidate = function (val) {
        return (/^\d{19}$/g.test(val));
    };

    /* 验证日期时间 */
    e.datetime = function (val) {
        return (/^[1-9]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\s+(20|21|22|23|[0-1]\d):[0-5]\d:[0-5]\d$/.test(val));
    };

    /* 验证邮箱地址 */
    e.email = function (val) {
        return (/^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))$/i
            .test(val));
    };

    /* 验证手机号码 */

    e.company = function(val) {

	}

    e.mobile = function (val) {
        return (/1\d{10}/.test($.trim(val)));
    };
	
	 /* 验证职业 */
	 e.post = function(val) {
        return (/1[34578]{1}\d{9}/.test($.trim(val)));
	};
	
	 /* 验证手机号码 */
    e.mobile = function (val) {
        return (/1\d{10}/.test($.trim(val)));
    };

    /* 验证座机号码 */
    e.telephone = function (val) {
        return (/^\(?(0\d{2,3}-?)?\)?\d{7,8}$/.test(val));
    };

    /* 邮政编码校验 */
    e.postCode = function (val) {
        return (/^\d{6}$/.test(val));
    };

    /* 验证字符串中包含空格 */
    e.hasNull = function(val){
        if(e.isNull(val)) return false;
        return /\s/.test(val);
    };
    
     /* 验证是否为空 */
    e.isYes = function (val) {
        return ($.trim(val) === 'Y');
    };


    /* 验证字符长度范围 */
    e.range = function (val, _min, _max) {
        return (eval('/^.{' + _min + ',' + _max + '}$/').test(val));

        //return (/[\u4E00-\u9FA5A-Za-z]{1,10}$/.test(val));
        //var length = val.replace(/[\u0391-\uFFE5]/g, "aa").length;
        //return length <= _max ? true : false;
    };

    /* 身份证验证  */
    e.idcard = function (val) {
        var idcard = val.toUpperCase();
        var area = {
            "11": "北京",
            "12": "天津",
            "13": "河北",
            "14": "山西",
            "15": "内蒙古",
            "21": "辽宁",
            "22": "吉林",
            "23": "黑龙江",
            "31": "上海",
            "32": "江苏",
            "33": "浙江",
            "34": "安徽",
            "35": "福建",
            "36": "江西",
            "37": "山东",
            "41": "河南",
            "42": "湖北",
            "43": "湖南",
            "44": "广东",
            "45": "广西",
            "46": "海南",
            "50": "重庆",
            "51": "四川",
            "52": "贵州",
            "53": "云南",
            "54": "西藏",
            "61": "陕西",
            "62": "甘肃",
            "63": "青海",
            "64": "宁夏",
            "65": "新疆",
            "71": "台湾",
            "81": "香港",
            "82": "澳门",
            "91": "国外"
        };
        var Y, JYM;
        var S, M;
        var idcard_array = [];
        idcard_array = idcard.split("");
        var ereg, eregNow;
        if (area[parseInt(idcard.substr(0, 2))] == null) {
            return false;
        }
        ;
        if (idcard.length === 18) {
            if (parseInt(idcard.substr(6, 4)) % 4 == 0 || (parseInt(idcard.substr(6, 4)) % 100 == 0 && parseInt(idcard.substr(6, 4)) % 4 == 0)) {
                ereg = /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/; // 闰年出生日期的合法性正则表达式
                eregNow = /^[1-9][0-9]{5}20[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/; // 闰年出生日期的合法性正则表达式
            } else {
                ereg = /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/; // 平年出生日期的合法性正则表达式
                eregNow = /^[1-9][0-9]{5}20[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/; // 平年出生日期的合法性正则表达式
            }
            ;

            if (ereg.test(idcard) || eregNow.test(idcard)) {
                S = (parseInt(idcard_array[0]) + parseInt(idcard_array[10])) * 7 + (parseInt(idcard_array[1]) + parseInt(idcard_array[11])) * 9 + (parseInt(idcard_array[2]) + parseInt(idcard_array[12])) * 10 + (parseInt(idcard_array[3]) + parseInt(idcard_array[13])) * 5 + (parseInt(idcard_array[4]) + parseInt(idcard_array[14])) * 8 + (parseInt(idcard_array[5]) + parseInt(idcard_array[15])) * 4 + (parseInt(idcard_array[6]) + parseInt(idcard_array[16])) * 2 + parseInt(idcard_array[7]) * 1 + parseInt(idcard_array[8]) * 6 + parseInt(idcard_array[9]) * 3;
                Y = S % 11;
                M = "F";
                JYM = "10X98765432";
                M = JYM.substr(Y, 1);
                return M == idcard_array[17] ? true : false;
            } else {
                return false;
            }
        }
        return false;
    };

    /*护照验证*/
    e.passport = function (val) {
		return /(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{2,18}$/.test(val);
    }

    /*出生证验证*/
    e.birth = function (val) {
       return /^[A-Z]{1}[0-9]{7}(11|12|13|14|15|21|22|23|31|32|33|34|35|36|37|41|42|43|44|45|46|50|51|52|53|54|61|62|63|64|65)$/g.test(val);
    }

    /*军人证验证*/
    e.armyman = function (val) {
        var flag;
        if (/^[A-Za-z]{10,18}$/g.test(val) || /^[0-9]{10,18}$/g.test(val)) {
            flag = false;
        } else {
            flag = /[a-zA-Z0-9]{10,18}/g.test(val);
        }
        return flag;
    }

    /*台胞证验证*/
    e.Taiwan = function (val) {
        return (/[0-9]{8}/g.test(val));
    }

    /*回乡证验证*/
    e.goHome = function (val) {
        if (val.length == 9) {
            return (/^[a-zA-Z]{1}[0-9]{8}/g.test(val));
        } else if(val.length == 11){
            return (/^[a-zA-Z]{1}[0-9]{10}$/g.test(val));
        }
    }

    e.checkMobiePho=function(str){
        // var myreg = /^[1][3,4,5,6,7,8][0-9]{9}$/;
        var myreg = /^1(3[0-9]|4[5,7]|5[0-9]|8[0-9]|9[8]|7[0,1,2,3,4,5,6,8,9])[0-9]{8}$/;
        if (!myreg.test(str)) {
            return false;
        } else {
            return true;
        }
    }
    /*姓名验证*/
    e.name = function (val) {
        return (/^[\u4e00-\u9fa5\·]{2,}$/g.test(val) || /^[a-zA-Z\'\s*]{2,}$/g.test(val));
    }

    /* 年龄校验 */
    e.age = function (val) {
        if(e.hasNull(val)) return false;
        return /^0$|(^[1-9][0-9]{0,2}$)/g.test(val);
    }

    /*整数金额验证*/
    e.money = function (val) {
        return (/^([1-9][0-9]*)$/g.test(val));
    }

    /*详细地址验证*/
    e.address = function (val) {
        return (/(?=[0-9a-zA-Z\u4e00-\u9fa5]+$)/g.test(val));
    }

    /*职业代码*/
    e.jobCode = function (val) {
        return (/[0-9]{7}/g.test(val));
    }

    /* 证件类型与号码匹配验证 */
    e.checkCard = function (val, type) {
        var _status = true;
        var _message = '';
        switch (type) {
            case "111":
                if (!validation.idcard(val)) {
                    _status = false;
                    _message = '请输入正确的身份证号码（18位居民身份证号码）！';
                }
                break;
            case "414":
                if (!validation.passport(val)) {
                    _status = false;
                    _message = '请输入正确的护照号码（英文+数字）！';
                }
                break;
            case "117":
                if (!validation.birth(val)) {
                    _status = false;
                    _message = '请输入正确的出生照号码（10位数字或者1位英文+10位数字）！';
                }
                break;
            case "114":
                if (!validation.armyman(val)) {
                    _status = false;
                    _message = '请输入正确的军人证号码（10~18字符，英文+数字）！';
                }
                break;
            case "511":
                if (!validation.Taiwan(val)) {
                    _status = false;
                    _message = '请输入正确的台胞证号码（8位数字）！';
                }
                break;
            case "516":
                if (!validation.goHome(val)) {
                    _status = false;
                    _message = '请输入正确的港澳台回乡证号码（1位英文+8位数字/1位英文+10位数字）！';
                }
                break;
        }
        ;
        return {'status': _status, 'message': _message};
    };

    e.checkVali = function (ipt, callback) {
        var _name = ipt.attr('name');  //获取 name值
        var _val = $.trim(ipt.val());  //获取 value值
        var _type = $('input[name=identityType]:checked').val(); // 表单提交时获取当前证件类型
        var _status = true;
        var message = '';
        if (_val === '') {
            return {"obj": ipt, "status": _status, "message": message};
        }
        switch (_name) {
            case 'name':
                _status = validation.name(_val);
                message = '姓名应为2位及以上中文或英文，请重新填写！';
                break;
           case 'refereeNoNself':
                _status = validation.idcard(_val);
                message = '请输入正确的身份证号码（18位居民身份证号码）！';
                break;
            case 'refereeName':
                _status = validation.name(_val);
                message = '推荐人姓名应为2位及以上中文或英文，请重新填写！';
                break;
            case 'refereeNameF':
                _status = validation.name(_val);
                message = '推荐人姓名应为2位及以上中文或英文，请重新填写！';
                break;
            case 'annualIncome':
                _status = validation.money(_val);
                message = '金额格式错误，请重新填写！';
            break;
            case 'ctfexpireDate':
                _status = validation.date(_val);
                message = '有效期格式不正确，请重新选择！';
                break;
            case 'birthday':
                _status = validation.date(_val);
                message = '出生日期格式不正确，请重新选择！';
                break;
            case 'identityCode':
                _status = validation.checkCard(_val, _type).status;
                message = validation.checkCard(_val, _type).message;
                break;
			case 'cellphone':
				_status = validation.checkMobiePho(_val);
				message = '签约人手机号不符合要求';
			break;
            case 'age':
                _status = validation.age(_val);
                message = '年龄格式不正确，请重新填写！';
                break;
			// case 'telephone':
			// 	_status = validation.mobile(_val);
			// 	message = '手机号格式不正确，请重新填写！';
			// break;
			case 'email':
				_status = validation.email(_val);
				message = '请输入正确的邮箱地址！';
			break;
			case 'jobCode':
				_status = validation.jobCode(_val);
				message = '请输入正确的职业代码！';
			break;
			case 'familyIncome':
				_status = validation.money(_val);
				message = '家庭全年收入格式不正确，请重新填写！';
			break;
			case 'personalIncome':
				_status = validation.money(_val);
				message = '个人全年收入格式不正确，请重新填写！';
			break;
			case 'familyAddrDetail':
				_status = validation.address(_val);
				message = '请填写投保人正确的家庭住址！';
			break;
			case 'workAddrDetail':
				_status = validation.address(_val);
				message = '请填写投保人正确的单位地址！';
			break;
			case 'familyPostcode':
				_status = validation.postCode(_val);
				message = '请填写投保人正确的家庭住址邮编！';
			break;
			case 'workPostcode':
				_status = validation.postCode(_val);
				message = '请填写投保人正确的单位地址邮编！';
			break;
			case 'unitName':
				_status = validation.address(_val);
				message = '请填写投保人正确的单位名称！';
			break;
			case 'plan-recommend':
				_status = validation.isYes(_val);
				message = '请阅读引荐关系告知书！';
			break;
			case 'plan-credit':
				_status = validation.isYes(_val);
				message = '请阅读个人信用承诺书！';
			break;
			case 'plan-important':
				_status = validation.isYes(_val);
				message = '请阅读招募代理制营销员重要告知书！';
			break;
		};
	
		return {"obj" : ipt, "status": _status, "message": message};
	};
           
    e.checkValiMust = function (form, callback) {
        var _status = true;
        var arr = form.serializeArray();
        for (var i in arr) {
            if (!arr[i].name) {
                continue;
            }
            var _obj = form.find('[name=' + arr[i].name + ']');
            if (_obj.data('must') === 1 && $.trim(_obj.val()) === '') {
                var _name = _obj.data('mustname');
                common.alertCreate({
                    html: _name + '是必填项，不能为空！',
                    callback: function () {
                        if(_obj.is('select')) {
                            _obj.prev('input').focus();
                        } else {
                            _obj.focus();
                        }
                    }
                });
                _status = false;
                break;
            }
            var _ipt = e.checkVali(_obj);
            if (!_ipt.status) {
                _status = _ipt.status;
                common.alertCreate({
                    html: _ipt.message,
                    callback: function () {
                        // _obj.focus();
                    }
                });
                break;
            }
        }

        return _status;
    };

    //判断证件名
    e.checkIdType=function (typenum) {
        var type='';
        if(typenum=='111'){
            type='身份证'
        }else if(typenum=='414'){
            type='护照'
        }else if(typenum='511'){
            type='台胞证'
        }else {
            type='港澳回乡证'
        }
        return type;
    };
    e.checkSex=function (sexCode) {
        var type='';
        if(sexCode=='M'){
            type='男'
        }else {
            type='女'
        }
        return type
    }
}(window.Zepto, window.validation);
