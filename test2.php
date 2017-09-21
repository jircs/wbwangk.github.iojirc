$s = <<<'EOF'
陈港生
元楼
蓝瘦
EOF;
header('Last-Modified: '.gmdate('D, d M Y H:i:s', time()).' GMT', true, 200);
header('ETag: "5816f349-19"');
echo $s;
