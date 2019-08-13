function getCrc(input) {
    var encoding = getBEncoding();

    var a = 0x0 ^ -0x1;
    for (var i = 0x0; i < input['length']; i++) {
        a = a >>> 0x8 ^ encoding[(a ^ input['charCodeAt'](i)) & 0xff];
    }

    var result = (a ^ -0x1) >>> 0x0;

    return result;
}

function getBEncoding() {
    var e;
    var c = [];
    for (var i = 0x0; i < 0x100; i++) {
        e = i;
        for(var j = 0x0; j < 0x8; j++) {
            e = e & 0x1 ? 0xedb88320 ^ e >>> 0x1 : e >>> 0x1;
        }
        c[i] = e;
    }

    return c;
}

function getCRCSign(input) {
    return getCrc('public-api' + input + 'WxvttruvF01cvHy8' + '8r5yD8pl8lLcz20G' + '8dY72lsSOvXnJF4T')
}

function getA(time) {
    return 'public-api';
}

function getB(time) {
    return getCrc(time + 'b').toString();
}

function getC(time) {
    return getCRCSign(time).toString();
}

function getD(time) {
    return getCrc(Date['now']() + 'd').toString();
}

function getE(time) {
    return Math['random']().toString();
}

function getT(time) {
    return time.toString();
}

function getX() {
    return (Math['random']() * 0xa).toString();
}

function getSignatureString() {
    return getEncodingString(Date['now']())
}

function getEncodingString(time) {
    return '&a=' + getA(time) + '&b=' + getB(time) + '&c=' + getC(time) + '&d=' + getD(time) + '&e=' + getE(time) + '&t=' + getT(time) + '&x=' + getX();
}