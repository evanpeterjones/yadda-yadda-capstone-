;;; lip.el --- a social media client for Emacs.

;; Copyright (C) 2015-2018  Free Software Foundation, Inc.

;; Author: Evan Jones <evanpeterjones@gmail.com>
;; Maintainer: &
;; URL: https://github.com/evanpeterjones/yadda-yadda/client/lip
;; Version: 0.0.01
;; Keywords: lip yapp yadda-yadda
;; Package-Requires: ((emacs "24"))

(if (version< emacs-version "24")
    (message "this package requires Emacs 24.4 or later"))

(defvar deps
  '(request tesssssss)
  "a growing list of needed dependencies ~redundant? lol")

(defun all (test-list)
  "recursively verify no values in a list are nil"
  (if test-list
      (and (car test-list) (and-all (cdr test-list)))
    t))

;; if any packages not installed, install
(if (not (all (map package-installed-p deps)))
    (mapc 'package-install deps))

(request "localhost:5000/feed" 
         :type "GET"
         :success (lambda (x) (print x))
         :error (lambda (x) (print x)))
