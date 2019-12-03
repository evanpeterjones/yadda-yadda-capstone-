;;; lip.el --- a social media client for Emacs.

;; Copyright (C) 2019-..  PluvioSoftware, Inc.

;; Author: Evan Jones <evanpeterjones@gmail.com>
;; Maintainer: &
;; URL: https://github.com/evanpeterjones/yadda-yadda/client/lip
;; Version: 0.0.01
;; Keywords: lip yapp yadda-yadda
;; Package-Requires: ((emacs "24"))

(if (version< emacs-version "24")
    (message "this package requires Emacs 24.4 or later"))

(defvar user-session-id nil)

(defvar deps
  '(request)
  "a growing list of needed dependencies ~redundant? lol")

(defun all (test-list)
  "recursively verify no values in a list are nil"
  (if test-list
      (and (car test-list) (and-all (cdr test-list)))
    t))

(require 'package)

(defun make-request (endpoint)
  (with-current-buffer (url-retrieve-synchronously endpoint)
    (progn 
      (print (buffer-string))
      (kill-buffer))))
